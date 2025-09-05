package com.blog.mavenserver.controller;

import com.blog.mavenserver.dto.*;
import com.blog.mavenserver.service.AuthService;
import com.blog.mavenserver.service.ConfigService;
import com.blog.mavenserver.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ConfigService configService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        logger.info("收到登录请求：{}", request.getLogin());
        
        LoginResponse response = authService.login(request);
        
        if (response.getSuccess()) {
            logger.info("登录请求处理成功：{}", request.getLogin());
        } else {
            logger.warn("登录请求处理失败：{}，原因：{}", request.getLogin(), response.getMessage());
        }
        
        return response;
    }
    
    @PostMapping("/register")
    public CommonResponse register(@RequestBody RegisterRequest request) {
        logger.info("收到注册请求：邮箱 {}", request.getEmail());
        
        CommonResponse response = authService.register(request);
        
        if (response.getSuccess()) {
            logger.info("注册请求处理成功：邮箱 {}", request.getEmail());
        } else {
            logger.warn("注册请求处理失败：邮箱 {}，原因：{}", request.getEmail(), response.getMessage());
        }
        
        return response;
    }
    
    @PostMapping("/send-code")
    public CommonResponse sendCode(@RequestBody SendCodeRequest request) {
        logger.info("收到发送验证码请求：邮箱 {}", request.getEmail());
        
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            logger.warn("发送验证码失败：邮箱为空");
            return new CommonResponse(false, "邮箱不能为空");
        }
        
        boolean success = emailService.sendVerificationCode(request.getEmail().trim());
        
        if (success) {
            logger.info("验证码发送成功：邮箱 {}", request.getEmail());
            return new CommonResponse(true, "验证码发送成功");
        } else {
            logger.warn("验证码发送失败：邮箱 {}", request.getEmail());
            return new CommonResponse(false, "验证码发送失败，请稍后重试");
        }
    }
    
    @PostMapping("/logout")
    public CommonResponse logout(@RequestBody LogoutRequest request) {
        logger.info("收到退出登录请求");
        
        CommonResponse response = authService.logout(request.getToken());
        
        if (response.getSuccess()) {
            logger.info("退出登录请求处理成功");
        } else {
            logger.warn("退出登录请求处理失败：{}", response.getMessage());
        }
        
        return response;
    }
    
    @PostMapping("/validate-token")
    public TokenValidateResponse validateToken(@RequestBody TokenValidateRequest request) {
        logger.info("收到Token验证请求");
        
        TokenValidateResponse response = authService.validateTokenWithUser(request);
        
        if (response.getSuccess() && response.getValid()) {
            logger.info("Token验证请求处理成功");
        } else {
            logger.warn("Token验证请求处理失败：{}", response.getMessage());
        }
        
        return response;
    }
    
    @PostMapping("/configs")
    public GetConfigsResponse getConfigs(@RequestBody GetConfigsRequest request) {
        logger.info("收到获取配置请求");
        
        // 验证管理员权限
        if (!authService.isAdminUser(request.getToken())) {
            logger.warn("获取配置失败：权限不足");
            return new GetConfigsResponse(false, "权限不足，仅管理员可访问");
        }
        
        var configs = configService.getAllConfigs();
        
        if (configs.isEmpty()) {
            logger.warn("获取配置失败：未找到配置项");
            return new GetConfigsResponse(false, "未找到配置项");
        }
        
        logger.info("获取配置成功：共 {} 条配置", configs.size());
        return new GetConfigsResponse(true, "获取配置成功", configs);
    }
}