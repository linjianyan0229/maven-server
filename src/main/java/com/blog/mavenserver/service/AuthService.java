package com.blog.mavenserver.service;

import com.blog.mavenserver.dto.*;
import com.blog.mavenserver.entity.Token;
import com.blog.mavenserver.entity.User;
import com.blog.mavenserver.repository.TokenRepository;
import com.blog.mavenserver.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TokenRepository tokenRepository;
    
    @Autowired
    private EmailService emailService;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public CommonResponse register(RegisterRequest request) {
        try {
            // 参数验证
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                logger.warn("注册失败：邮箱为空");
                return new CommonResponse(false, "邮箱不能为空");
            }
            
            if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                logger.warn("注册失败：验证码为空，邮箱：{}", request.getEmail());
                return new CommonResponse(false, "验证码不能为空");
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                logger.warn("注册失败：密码为空，邮箱：{}", request.getEmail());
                return new CommonResponse(false, "密码不能为空");
            }
            
            if (request.getConfirmPassword() == null || !request.getPassword().equals(request.getConfirmPassword())) {
                logger.warn("注册失败：两次密码不一致，邮箱：{}", request.getEmail());
                return new CommonResponse(false, "两次输入的密码不一致");
            }

            // 密码长度检查
            if (request.getPassword().length() < 6) {
                logger.warn("注册失败：密码长度不足，邮箱：{}", request.getEmail());
                return new CommonResponse(false, "密码长度不能少于6位");
            }

            // 验证邮箱格式
            if (!request.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                logger.warn("注册失败：邮箱格式不正确，邮箱：{}", request.getEmail());
                return new CommonResponse(false, "邮箱格式不正确");
            }

            // 检查邮箱是否已存在
            if (userRepository.existsByEmail(request.getEmail().trim())) {
                logger.warn("注册失败：邮箱已存在，邮箱：{}", request.getEmail());
                return new CommonResponse(false, "该邮箱已被注册");
            }

            // 验证验证码（注册类型）
            if (!emailService.verifyCode(request.getEmail().trim(), request.getCode().trim(), "注册")) {
                logger.warn("注册失败：验证码错误或已过期，邮箱：{}", request.getEmail());
                return new CommonResponse(false, "验证码错误或已过期");
            }

            // 生成用户名（从邮箱提取）
            String username = generateUsernameFromEmail(request.getEmail().trim());
            
            // 创建用户
            User user = new User();
            user.setUsername(username);
            user.setEmail(request.getEmail().trim());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setSex(0); // 默认未知
            user.setRole("USER"); // 默认普通用户
            user.setStatus("正常"); // 默认正常状态
            user.setCreatedTime(LocalDateTime.now());

            userRepository.save(user);

            logger.info("用户注册成功：邮箱 {}，用户名 {}", user.getEmail(), user.getUsername());
            return new CommonResponse(true, "注册成功");

        } catch (Exception e) {
            logger.error("注册过程发生异常：{}", e.getMessage(), e);
            return new CommonResponse(false, "注册失败，系统异常");
        }
    }

    private String generateUsernameFromEmail(String email) {
        String base = email.substring(0, email.indexOf("@"));
        
        // 检查用户名是否已存在
        String username = base;
        int counter = 1;
        while (userRepository.existsByUsername(username)) {
            username = base + counter;
            counter++;
        }
        
        return username;
    }

    public LoginResponse login(LoginRequest request) {
        try {
            // 参数验证
            if (request.getLogin() == null || request.getLogin().trim().isEmpty()) {
                logger.warn("登录失败：用户名或邮箱为空");
                return new LoginResponse(false, "用户名或邮箱不能为空");
            }
            
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                logger.warn("登录失败：密码为空，用户：{}", request.getLogin());
                return new LoginResponse(false, "密码不能为空");
            }

            // 查找用户
            Optional<User> userOpt = userRepository.findByUsernameOrEmail(request.getLogin().trim());
            if (!userOpt.isPresent()) {
                logger.warn("登录失败：用户不存在，输入：{}", request.getLogin());
                return new LoginResponse(false, "用户名或密码错误");
            }

            User user = userOpt.get();
            
            // 检查用户状态
            if (!"正常".equals(user.getStatus())) {
                logger.warn("登录失败：用户状态异常，用户：{}，状态：{}", user.getUsername(), user.getStatus());
                return new LoginResponse(false, "账户状态异常，无法登录");
            }

            // 验证密码
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                logger.warn("登录失败：密码错误，用户：{}", user.getUsername());
                return new LoginResponse(false, "用户名或密码错误");
            }

            // 清除该用户的所有旧Token
            tokenRepository.deleteByUserId(user.getId());

            // 生成新Token
            String tokenValue = UUID.randomUUID().toString().replace("-", "");
            
            // 根据"记住我"设置过期时间
            LocalDateTime expireTime;
            if (Boolean.TRUE.equals(request.getRememberMe())) {
                expireTime = LocalDateTime.now().plusDays(7); // 7天
                logger.info("用户登录成功（记住我7天）：{}", user.getUsername());
            } else {
                expireTime = LocalDateTime.now().plusDays(1); // 1天
                logger.info("用户登录成功（有效期1天）：{}", user.getUsername());
            }

            // 保存Token
            Token token = new Token(user.getId(), tokenValue, expireTime);
            tokenRepository.save(token);

            // 更新用户登录时间
            user.setLoginTime(LocalDateTime.now());
            userRepository.save(user);

            logger.info("用户登录成功：{}，Token过期时间：{}", user.getUsername(), expireTime);
            return new LoginResponse(true, "登录成功", tokenValue, expireTime, user);

        } catch (Exception e) {
            logger.error("登录过程发生异常：{}", e.getMessage(), e);
            return new LoginResponse(false, "登录失败，系统异常");
        }
    }

    public boolean validateToken(String tokenValue) {
        if (tokenValue == null || tokenValue.trim().isEmpty()) {
            return false;
        }
        
        Optional<Token> tokenOpt = tokenRepository.findValidToken(tokenValue.trim(), LocalDateTime.now());
        return tokenOpt.isPresent();
    }

    public Optional<User> getUserByToken(String tokenValue) {
        if (tokenValue == null || tokenValue.trim().isEmpty()) {
            return Optional.empty();
        }
        
        Optional<Token> tokenOpt = tokenRepository.findValidToken(tokenValue.trim(), LocalDateTime.now());
        if (tokenOpt.isPresent()) {
            return userRepository.findById(tokenOpt.get().getUserId());
        }
        return Optional.empty();
    }
    
    public CommonResponse logout(String tokenValue) {
        try {
            if (tokenValue == null || tokenValue.trim().isEmpty()) {
                logger.warn("退出登录失败：Token为空");
                return new CommonResponse(false, "Token不能为空");
            }
            
            Optional<Token> tokenOpt = tokenRepository.findByToken(tokenValue.trim());
            if (tokenOpt.isPresent()) {
                Token token = tokenOpt.get();
                tokenRepository.delete(token);
                
                // 获取用户信息用于日志
                Optional<User> userOpt = userRepository.findById(token.getUserId());
                if (userOpt.isPresent()) {
                    logger.info("用户退出登录成功：{}", userOpt.get().getUsername());
                } else {
                    logger.info("用户退出登录成功：用户ID {}", token.getUserId());
                }
                
                return new CommonResponse(true, "退出登录成功");
            } else {
                logger.warn("退出登录失败：Token无效或已过期");
                return new CommonResponse(false, "Token无效或已过期");
            }
        } catch (Exception e) {
            logger.error("退出登录过程发生异常：{}", e.getMessage(), e);
            return new CommonResponse(false, "退出登录失败，系统异常");
        }
    }

    public TokenValidateResponse validateTokenWithUser(TokenValidateRequest request) {
        try {
            if (request.getToken() == null || request.getToken().trim().isEmpty()) {
                logger.warn("Token验证失败：Token为空");
                return new TokenValidateResponse(false, "Token不能为空", false);
            }

            Optional<Token> tokenOpt = tokenRepository.findValidToken(request.getToken().trim(), LocalDateTime.now());
            if (tokenOpt.isPresent()) {
                Token token = tokenOpt.get();
                Optional<User> userOpt = userRepository.findById(token.getUserId());
                
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    
                    // 检查用户状态
                    if (!"正常".equals(user.getStatus())) {
                        logger.warn("Token验证失败：用户状态异常，用户：{}，状态：{}", user.getUsername(), user.getStatus());
                        return new TokenValidateResponse(true, "用户状态异常", false);
                    }
                    
                    logger.info("Token验证成功：用户 {}", user.getUsername());
                    return new TokenValidateResponse(true, "Token验证成功", true, user);
                } else {
                    logger.warn("Token验证失败：用户不存在，用户ID：{}", token.getUserId());
                    return new TokenValidateResponse(true, "用户不存在", false);
                }
            } else {
                logger.warn("Token验证失败：Token无效或已过期");
                return new TokenValidateResponse(true, "Token无效或已过期", false);
            }

        } catch (Exception e) {
            logger.error("Token验证过程发生异常：{}", e.getMessage(), e);
            return new TokenValidateResponse(false, "Token验证失败，系统异常", false);
        }
    }

    public boolean isAdminUser(String tokenValue) {
        try {
            if (tokenValue == null || tokenValue.trim().isEmpty()) {
                return false;
            }

            Optional<Token> tokenOpt = tokenRepository.findValidToken(tokenValue.trim(), LocalDateTime.now());
            if (tokenOpt.isPresent()) {
                Token token = tokenOpt.get();
                Optional<User> userOpt = userRepository.findById(token.getUserId());
                
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    boolean isAdmin = "ADMIN".equals(user.getRole()) && "正常".equals(user.getStatus());
                    
                    if (isAdmin) {
                        logger.info("管理员权限验证成功：用户 {}", user.getUsername());
                    } else {
                        logger.warn("管理员权限验证失败：用户 {}，角色 {}，状态 {}", 
                            user.getUsername(), user.getRole(), user.getStatus());
                    }
                    
                    return isAdmin;
                }
            }
            
            logger.warn("管理员权限验证失败：Token无效或用户不存在");
            return false;
            
        } catch (Exception e) {
            logger.error("管理员权限验证异常：{}", e.getMessage(), e);
            return false;
        }
    }
    
    public CommonResponse resetPassword(ResetPasswordRequest request) {
        try {
            // 参数验证
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                logger.warn("重置密码失败：邮箱为空");
                return new CommonResponse(false, "邮箱不能为空");
            }
            
            if (request.getCode() == null || request.getCode().trim().isEmpty()) {
                logger.warn("重置密码失败：验证码为空，邮箱：{}", request.getEmail());
                return new CommonResponse(false, "验证码不能为空");
            }
            
            if (request.getNewPassword() == null || request.getNewPassword().trim().isEmpty()) {
                logger.warn("重置密码失败：新密码为空，邮箱：{}", request.getEmail());
                return new CommonResponse(false, "新密码不能为空");
            }
            
            if (request.getNewPassword().length() < 6) {
                logger.warn("重置密码失败：新密码长度不足，邮箱：{}", request.getEmail());
                return new CommonResponse(false, "新密码不能少于6位");
            }
            
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                logger.warn("重置密码失败：两次密码输入不一致，邮箱：{}", request.getEmail());
                return new CommonResponse(false, "两次密码输入不一致");
            }

            // 验证用户是否存在
            Optional<User> userOpt = userRepository.findByEmail(request.getEmail().trim());
            if (!userOpt.isPresent()) {
                logger.warn("重置密码失败：用户不存在，邮箱：{}", request.getEmail());
                return new CommonResponse(false, "该邮箱未注册");
            }
            
            User user = userOpt.get();
            
            // 检查用户状态
            if (!"正常".equals(user.getStatus())) {
                logger.warn("重置密码失败：用户状态异常，邮箱：{}，状态：{}", request.getEmail(), user.getStatus());
                return new CommonResponse(false, "账户状态异常，无法重置密码");
            }

            // 验证验证码（修改类型）
            if (!emailService.verifyCode(request.getEmail().trim(), request.getCode().trim(), "修改")) {
                logger.warn("重置密码失败：验证码错误或已过期，邮箱：{}", request.getEmail());
                return new CommonResponse(false, "验证码错误或已过期");
            }

            // 加密新密码
            String encodedPassword = passwordEncoder.encode(request.getNewPassword().trim());
            
            // 更新密码
            user.setPassword(encodedPassword);
            userRepository.save(user);
            
            // 清除该用户的所有Token（强制重新登录）
            tokenRepository.deleteByUserId(user.getId());
            
            logger.info("密码重置成功：邮箱 {}，用户 {}", request.getEmail(), user.getUsername());
            return new CommonResponse(true, "密码重置成功");

        } catch (Exception e) {
            logger.error("密码重置异常：邮箱 {}，错误信息：{}", request.getEmail(), e.getMessage(), e);
            return new CommonResponse(false, "密码重置失败，请稍后重试");
        }
    }
    
    public UserProfileResponse getUserProfile(UserProfileRequest request) {
        try {
            if (request.getToken() == null || request.getToken().trim().isEmpty()) {
                logger.warn("获取个人信息失败：Token为空");
                return new UserProfileResponse(false, "Token不能为空");
            }
            
            // 验证Token并获取用户
            Optional<Token> tokenOpt = tokenRepository.findByToken(request.getToken().trim());
            if (!tokenOpt.isPresent()) {
                logger.warn("获取个人信息失败：Token无效");
                return new UserProfileResponse(false, "Token无效");
            }
            
            Token token = tokenOpt.get();
            
            // 检查Token是否过期
            if (token.getExpireTime().isBefore(LocalDateTime.now())) {
                logger.warn("获取个人信息失败：Token已过期");
                // 删除过期Token
                tokenRepository.delete(token);
                return new UserProfileResponse(false, "Token已过期");
            }
            
            // 获取用户信息
            Optional<User> userOpt = userRepository.findById(token.getUserId());
            if (!userOpt.isPresent()) {
                logger.warn("获取个人信息失败：用户不存在，用户ID：{}", token.getUserId());
                return new UserProfileResponse(false, "用户不存在");
            }
            
            User user = userOpt.get();
            
            // 检查用户状态
            if (!"正常".equals(user.getStatus())) {
                logger.warn("获取个人信息失败：用户状态异常，用户：{}，状态：{}", user.getUsername(), user.getStatus());
                return new UserProfileResponse(false, "账户状态异常");
            }
            
            // 构建用户信息（不包含密码）
            UserProfileInfo userProfile = new UserProfileInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatar(),
                user.getSex(),
                user.getBio(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedTime(),
                user.getLoginTime()
            );
            
            logger.info("获取个人信息成功：用户 {}", user.getUsername());
            return new UserProfileResponse(true, "获取个人信息成功", userProfile);
            
        } catch (Exception e) {
            logger.error("获取个人信息异常：{}", e.getMessage(), e);
            return new UserProfileResponse(false, "获取个人信息失败，请稍后重试");
        }
    }
    
    public UpdateProfileResponse updateUserProfile(UpdateProfileRequest request) {
        try {
            if (request.getToken() == null || request.getToken().trim().isEmpty()) {
                logger.warn("修改个人信息失败：Token为空");
                return new UpdateProfileResponse(false, "Token不能为空");
            }
            
            // 验证Token并获取用户
            Optional<Token> tokenOpt = tokenRepository.findByToken(request.getToken().trim());
            if (!tokenOpt.isPresent()) {
                logger.warn("修改个人信息失败：Token无效");
                return new UpdateProfileResponse(false, "Token无效");
            }
            
            Token token = tokenOpt.get();
            
            // 检查Token是否过期
            if (token.getExpireTime().isBefore(LocalDateTime.now())) {
                logger.warn("修改个人信息失败：Token已过期");
                // 删除过期Token
                tokenRepository.delete(token);
                return new UpdateProfileResponse(false, "Token已过期");
            }
            
            // 获取用户信息
            Optional<User> userOpt = userRepository.findById(token.getUserId());
            if (!userOpt.isPresent()) {
                logger.warn("修改个人信息失败：用户不存在，用户ID：{}", token.getUserId());
                return new UpdateProfileResponse(false, "用户不存在");
            }
            
            User user = userOpt.get();
            
            // 检查用户状态
            if (!"正常".equals(user.getStatus())) {
                logger.warn("修改个人信息失败：用户状态异常，用户：{}，状态：{}", user.getUsername(), user.getStatus());
                return new UpdateProfileResponse(false, "账户状态异常");
            }
            
            // 验证用户名
            if (request.getUsername() != null && !request.getUsername().trim().isEmpty()) {
                String newUsername = request.getUsername().trim();
                
                // 检查用户名长度
                if (newUsername.length() < 2 || newUsername.length() > 50) {
                    logger.warn("修改个人信息失败：用户名长度不符合要求，用户：{}，新用户名：{}", user.getUsername(), newUsername);
                    return new UpdateProfileResponse(false, "用户名长度必须在2-50个字符之间");
                }
                
                // 检查用户名是否已被使用（排除自己）
                Optional<User> existingUser = userRepository.findByUsername(newUsername);
                if (existingUser.isPresent() && !existingUser.get().getId().equals(user.getId())) {
                    logger.warn("修改个人信息失败：用户名已存在，用户：{}，新用户名：{}", user.getUsername(), newUsername);
                    return new UpdateProfileResponse(false, "用户名已被使用");
                }
                
                user.setUsername(newUsername);
            }
            
            // 验证性别
            if (request.getSex() != null) {
                if (request.getSex() < 0 || request.getSex() > 2) {
                    logger.warn("修改个人信息失败：性别值无效，用户：{}，性别值：{}", user.getUsername(), request.getSex());
                    return new UpdateProfileResponse(false, "性别值无效，只能是0（未知）、1（男）、2（女）");
                }
                user.setSex(request.getSex());
            }
            
            // 更新头像
            if (request.getAvatar() != null) {
                user.setAvatar(request.getAvatar().trim().isEmpty() ? null : request.getAvatar().trim());
            }
            
            // 更新个人介绍
            if (request.getBio() != null) {
                String bio = request.getBio().trim();
                if (bio.length() > 500) {
                    logger.warn("修改个人信息失败：个人介绍过长，用户：{}，长度：{}", user.getUsername(), bio.length());
                    return new UpdateProfileResponse(false, "个人介绍不能超过500个字符");
                }
                user.setBio(bio.isEmpty() ? null : bio);
            }
            
            // 保存用户信息
            userRepository.save(user);
            
            // 构建更新后的用户信息
            UserProfileInfo userProfile = new UserProfileInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getAvatar(),
                user.getSex(),
                user.getBio(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedTime(),
                user.getLoginTime()
            );
            
            logger.info("修改个人信息成功：用户 {}", user.getUsername());
            return new UpdateProfileResponse(true, "修改个人信息成功", userProfile);
            
        } catch (Exception e) {
            logger.error("修改个人信息异常：{}", e.getMessage(), e);
            return new UpdateProfileResponse(false, "修改个人信息失败，请稍后重试");
        }
    }
    
    public UserListResponse getUserList(UserListRequest request) {
        try {
            if (request.getToken() == null || request.getToken().trim().isEmpty()) {
                logger.warn("获取用户列表失败：Token为空");
                return new UserListResponse(false, "Token不能为空");
            }
            
            // 验证管理员权限
            if (!isAdminUser(request.getToken())) {
                logger.warn("获取用户列表失败：权限不足");
                return new UserListResponse(false, "权限不足，仅管理员可访问");
            }
            
            // 获取所有用户列表
            List<User> users = userRepository.findAll();
            
            // 转换为UserInfo列表（不包含密码信息）
            List<UserInfo> userInfoList = users.stream()
                .map(user -> new UserInfo(
                    user.getId(),
                    user.getUsername(), 
                    user.getEmail(),
                    user.getAvatar(),
                    user.getSex(),
                    user.getBio(),
                    user.getRole(),
                    user.getStatus(),
                    user.getCreatedTime(),
                    user.getLoginTime()
                ))
                .collect(Collectors.toList());
            
            long totalCount = users.size();
            
            logger.info("获取用户列表成功：共 {} 名用户", totalCount);
            return new UserListResponse(true, "获取用户列表成功", userInfoList, totalCount);
            
        } catch (Exception e) {
            logger.error("获取用户列表异常：{}", e.getMessage(), e);
            return new UserListResponse(false, "获取用户列表失败，请稍后重试");
        }
    }
    
    public CommonResponse deleteUser(DeleteUserRequest request) {
        try {
            if (request.getToken() == null || request.getToken().trim().isEmpty()) {
                logger.warn("删除用户失败：Token为空");
                return new CommonResponse(false, "Token不能为空");
            }
            
            if (request.getUserId() == null) {
                logger.warn("删除用户失败：用户ID为空");
                return new CommonResponse(false, "用户ID不能为空");
            }
            
            // 验证管理员权限
            if (!isAdminUser(request.getToken())) {
                logger.warn("删除用户失败：权限不足");
                return new CommonResponse(false, "权限不足，仅管理员可访问");
            }
            
            // 检查要删除的用户是否存在
            Optional<User> userOpt = userRepository.findById(request.getUserId());
            if (!userOpt.isPresent()) {
                logger.warn("删除用户失败：用户不存在，用户ID：{}", request.getUserId());
                return new CommonResponse(false, "用户不存在");
            }
            
            User userToDelete = userOpt.get();
            
            // 检查是否尝试删除自己
            Optional<User> adminUserOpt = getUserByToken(request.getToken());
            if (adminUserOpt.isPresent() && adminUserOpt.get().getId().equals(request.getUserId())) {
                logger.warn("删除用户失败：不能删除自己，管理员用户：{}，尝试删除用户ID：{}", 
                    adminUserOpt.get().getUsername(), request.getUserId());
                return new CommonResponse(false, "不能删除自己的账户");
            }
            
            // 检查是否是唯一的管理员账户
            if ("ADMIN".equals(userToDelete.getRole())) {
                List<User> adminUsers = userRepository.findByRole("ADMIN");
                if (adminUsers.size() <= 1) {
                    logger.warn("删除用户失败：不能删除唯一的管理员账户，用户：{}", userToDelete.getUsername());
                    return new CommonResponse(false, "不能删除唯一的管理员账户");
                }
            }
            
            // 删除用户相关的Token（级联删除会自动处理）
            tokenRepository.deleteByUserId(request.getUserId());
            
            // 删除用户
            userRepository.delete(userToDelete);
            
            logger.info("删除用户成功：用户ID {}，用户名 {}，邮箱 {}", 
                userToDelete.getId(), userToDelete.getUsername(), userToDelete.getEmail());
            return new CommonResponse(true, "用户删除成功");
            
        } catch (Exception e) {
            logger.error("删除用户异常：{}", e.getMessage(), e);
            return new CommonResponse(false, "删除用户失败，请稍后重试");
        }
    }
    
    public UpdateUserRoleResponse updateUserRole(UpdateUserRoleRequest request) {
        try {
            // 参数验证
            if (request.getToken() == null || request.getToken().trim().isEmpty()) {
                logger.warn("修改用户角色失败：Token为空");
                return new UpdateUserRoleResponse(false, "Token不能为空");
            }
            
            if (request.getUserId() == null) {
                logger.warn("修改用户角色失败：用户ID为空");
                return new UpdateUserRoleResponse(false, "用户ID不能为空");
            }
            
            if (request.getRole() == null || request.getRole().trim().isEmpty()) {
                logger.warn("修改用户角色失败：角色为空");
                return new UpdateUserRoleResponse(false, "角色不能为空");
            }
            
            String role = request.getRole().trim();
            if (!"USER".equals(role) && !"ADMIN".equals(role)) {
                logger.warn("修改用户角色失败：角色值无效：{}", role);
                return new UpdateUserRoleResponse(false, "角色值无效，只能是USER或ADMIN");
            }
            
            // 验证管理员权限
            if (!isAdminUser(request.getToken())) {
                logger.warn("修改用户角色失败：权限不足");
                return new UpdateUserRoleResponse(false, "权限不足，仅管理员可访问");
            }
            
            // 检查要修改的用户是否存在
            Optional<User> userOpt = userRepository.findById(request.getUserId());
            if (!userOpt.isPresent()) {
                logger.warn("修改用户角色失败：用户不存在，用户ID：{}", request.getUserId());
                return new UpdateUserRoleResponse(false, "用户不存在");
            }
            
            User userToUpdate = userOpt.get();
            String originalRole = userToUpdate.getRole();
            
            // 检查是否尝试修改自己的角色
            Optional<User> adminUserOpt = getUserByToken(request.getToken());
            if (adminUserOpt.isPresent() && adminUserOpt.get().getId().equals(request.getUserId())) {
                logger.warn("修改用户角色失败：不能修改自己的角色，管理员用户：{}，尝试修改用户ID：{}", 
                    adminUserOpt.get().getUsername(), request.getUserId());
                return new UpdateUserRoleResponse(false, "不能修改自己的角色");
            }
            
            // 如果要将管理员降级为普通用户，检查是否是唯一的管理员
            if ("ADMIN".equals(originalRole) && "USER".equals(role)) {
                List<User> adminUsers = userRepository.findByRole("ADMIN");
                if (adminUsers.size() <= 1) {
                    logger.warn("修改用户角色失败：不能将唯一的管理员降级为普通用户，用户：{}", userToUpdate.getUsername());
                    return new UpdateUserRoleResponse(false, "不能将唯一的管理员降级为普通用户");
                }
            }
            
            // 检查角色是否已经相同
            if (role.equals(originalRole)) {
                logger.warn("修改用户角色失败：用户角色已经是{}，用户：{}", role, userToUpdate.getUsername());
                return new UpdateUserRoleResponse(false, "用户角色已经是" + ("ADMIN".equals(role) ? "管理员" : "普通用户"));
            }
            
            // 更新用户角色
            userToUpdate.setRole(role);
            User updatedUser = userRepository.save(userToUpdate);
            
            // 构建返回的用户信息
            UserInfo userInfo = new UserInfo();
            userInfo.setId(updatedUser.getId());
            userInfo.setUsername(updatedUser.getUsername());
            userInfo.setEmail(updatedUser.getEmail());
            userInfo.setAvatar(updatedUser.getAvatar());
            userInfo.setSex(updatedUser.getSex());
            userInfo.setBio(updatedUser.getBio());
            userInfo.setRole(updatedUser.getRole());
            userInfo.setStatus(updatedUser.getStatus());
            
            String roleChangeDesc = "ADMIN".equals(role) ? "管理员" : "普通用户";
            String originalRoleDesc = "ADMIN".equals(originalRole) ? "管理员" : "普通用户";
            
            logger.info("修改用户角色成功：用户ID {}，用户名 {}，角色从 {} 修改为 {}", 
                updatedUser.getId(), updatedUser.getUsername(), originalRoleDesc, roleChangeDesc);
            
            return new UpdateUserRoleResponse(true, "用户角色修改成功", userInfo);
            
        } catch (Exception e) {
            logger.error("修改用户角色异常：{}", e.getMessage(), e);
            return new UpdateUserRoleResponse(false, "修改用户角色失败，请稍后重试");
        }
    }
}