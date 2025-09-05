package com.blog.mavenserver.service;

import com.blog.mavenserver.entity.VerificationCode;
import com.blog.mavenserver.repository.VerificationCodeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;

@Service
@Transactional
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private VerificationCodeRepository verificationCodeRepository;
    
    @Autowired
    private ConfigService configService;
    
    private final Random random = new Random();

    public boolean sendVerificationCode(String email) {
        return sendVerificationCode(email, "注册");
    }
    
    public boolean sendVerificationCode(String email, String type) {
        try {
            // 检查邮箱格式
            if (!isValidEmail(email)) {
                logger.warn("发送验证码失败：邮箱格式不正确，邮箱：{}", email);
                return false;
            }

            // 检查是否频繁发送（1分钟内不能重复发送同类型验证码）
            if (verificationCodeRepository.existsByEmailAndTypeAndExpireTimeAfter(email, type, LocalDateTime.now().minusMinutes(4))) {
                logger.warn("发送验证码失败：发送过于频繁，邮箱：{}，类型：{}", email, type);
                return false;
            }

            // 根据类型获取不同的邮件配置
            ConfigService.EmailConfig emailConfig = getEmailConfigByType(type);
            if (emailConfig == null) {
                logger.error("发送验证码失败：邮件配置获取失败，邮箱：{}，类型：{}", email, type);
                return false;
            }

            // 动态配置邮件发送器
            configureMailSender(emailConfig);

            // 生成6位随机验证码
            String code = generateVerificationCode();
            
            // 设置过期时间（5分钟后过期）
            LocalDateTime expireTime = LocalDateTime.now().plusMinutes(5);

            // 保存验证码到数据库
            VerificationCode verificationCode = new VerificationCode(email, code, type, expireTime);
            verificationCodeRepository.save(verificationCode);

            // 发送邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailConfig.getAccount());
            message.setTo(email);
            message.setSubject(emailConfig.getSubject());
            
            // 替换验证码占位符
            String emailContent = emailConfig.getContent().replace("{code}", code);
            message.setText(emailContent);

            mailSender.send(message);
            
            logger.info("验证码发送成功：邮箱 {}，类型 {}，验证码 {}，过期时间 {}", email, type, code, expireTime);
            return true;

        } catch (Exception e) {
            logger.error("发送验证码异常：邮箱 {}，类型 {}，错误信息：{}", email, type, e.getMessage(), e);
            return false;
        }
    }

    private void configureMailSender(ConfigService.EmailConfig emailConfig) {
        if (mailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;
            
            // 动态设置邮件服务器配置
            mailSenderImpl.setHost(emailConfig.getHost());
            mailSenderImpl.setPort(emailConfig.getPort());
            mailSenderImpl.setUsername(emailConfig.getAccount());
            mailSenderImpl.setPassword(emailConfig.getPassword());
            
            // 设置邮件属性
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.timeout", "10000");
            props.put("mail.smtp.connectiontimeout", "10000");
            
            // 如果是465端口，启用SSL
            if (emailConfig.getPort() == 465) {
                props.put("mail.smtp.ssl.enable", "true");
            }
            
            mailSenderImpl.setJavaMailProperties(props);
            
            logger.debug("邮件发送器配置完成：host={}, port={}, username={}", 
                emailConfig.getHost(), emailConfig.getPort(), emailConfig.getAccount());
        }
    }

    private ConfigService.EmailConfig getEmailConfigByType(String type) {
        try {
            String account = configService.getConfigValue("email_account");
            String password = configService.getConfigValue("email_password");
            String host = configService.getConfigValue("email_host");
            String port = configService.getConfigValue("email_port", "587");
            
            String subject;
            String content;
            
            if ("修改".equals(type)) {
                subject = configService.getConfigValue("reset_password_email_subject", "【博客系统】密码重置验证码");
                content = configService.getConfigValue("reset_password_email_content", 
                    "您的密码重置验证码是：{code}\n\n验证码有效期为5分钟，请及时使用。\n如果这不是您的操作，请忽略此邮件。\n\n此邮件由系统自动发送，请勿回复。");
            } else {
                // 默认为注册类型
                subject = configService.getConfigValue("register_email_subject", "【博客系统】邮箱验证码");
                content = configService.getConfigValue("register_email_content", 
                    "您的邮箱验证码是：{code}\n\n验证码有效期为5分钟，请及时使用。\n如果这不是您的操作，请忽略此邮件。\n\n此邮件由系统自动发送，请勿回复。");
            }

            if (account == null || password == null || host == null) {
                logger.error("邮件配置不完整：account={}, password={}, host={}", 
                    account != null ? "已配置" : "未配置", 
                    password != null ? "已配置" : "未配置", 
                    host != null ? "已配置" : "未配置");
                return null;
            }

            ConfigService.EmailConfig emailConfig = new ConfigService.EmailConfig();
            emailConfig.setAccount(account);
            emailConfig.setPassword(password);
            emailConfig.setHost(host);
            emailConfig.setPort(Integer.parseInt(port));
            emailConfig.setSubject(subject);
            emailConfig.setContent(content);

            logger.info("邮件配置获取成功：类型 {}，host={}, port={}, account={}", type, host, port, account);
            return emailConfig;

        } catch (Exception e) {
            logger.error("获取邮件配置异常：类型 {}，错误信息：{}", type, e.getMessage(), e);
            return null;
        }
    }

    public boolean verifyCode(String email, String code) {
        return verifyCode(email, code, "注册");
    }
    
    public boolean verifyCode(String email, String code, String type) {
        try {
            if (email == null || code == null || email.trim().isEmpty() || code.trim().isEmpty()) {
                return false;
            }

            var verificationCodeOpt = verificationCodeRepository.findValidCode(
                email.trim(), code.trim(), type, LocalDateTime.now());
            
            if (verificationCodeOpt.isPresent()) {
                // 验证成功后删除验证码
                verificationCodeRepository.delete(verificationCodeOpt.get());
                logger.info("验证码验证成功：邮箱 {}，类型 {}", email, type);
                return true;
            } else {
                logger.warn("验证码验证失败：邮箱 {}，验证码 {}，类型 {}", email, code, type);
                return false;
            }

        } catch (Exception e) {
            logger.error("验证码验证异常：邮箱 {}，类型 {}，错误信息：{}", email, type, e.getMessage(), e);
            return false;
        }
    }

    private String generateVerificationCode() {
        return String.format("%06d", random.nextInt(1000000));
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
    
    // 定时清理过期验证码（可以通过定时任务调用）
    public void cleanExpiredCodes() {
        try {
            verificationCodeRepository.deleteExpiredCodes(LocalDateTime.now());
            logger.info("过期验证码清理完成");
        } catch (Exception e) {
            logger.error("清理过期验证码异常：{}", e.getMessage(), e);
        }
    }
}