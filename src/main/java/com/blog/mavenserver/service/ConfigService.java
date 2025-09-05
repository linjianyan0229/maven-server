package com.blog.mavenserver.service;

import com.blog.mavenserver.entity.Config;
import com.blog.mavenserver.repository.ConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ConfigService.class);
    
    @Autowired
    private ConfigRepository configRepository;

    /**
     * 根据配置键获取配置值
     */
    public String getConfigValue(String configKey) {
        try {
            Optional<String> valueOpt = configRepository.findValueByConfigKey(configKey);
            if (valueOpt.isPresent()) {
                logger.debug("获取配置成功：{} = {}", configKey, valueOpt.get());
                return valueOpt.get();
            } else {
                logger.warn("配置项不存在：{}", configKey);
                return null;
            }
        } catch (Exception e) {
            logger.error("获取配置项异常：{}，错误信息：{}", configKey, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 根据配置键获取配置值，如果不存在则返回默认值
     */
    public String getConfigValue(String configKey, String defaultValue) {
        String value = getConfigValue(configKey);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取邮件相关配置
     */
    public EmailConfig getEmailConfig() {
        try {
            String account = getConfigValue("email_account");
            String password = getConfigValue("email_password");
            String host = getConfigValue("email_host");
            String port = getConfigValue("email_port", "587");
            String subject = getConfigValue("register_email_subject", "【博客系统】邮箱验证码");
            String content = getConfigValue("register_email_content", 
                "您的邮箱验证码是：{code}\n\n验证码有效期为5分钟，请及时使用。\n如果这不是您的操作，请忽略此邮件。\n\n此邮件由系统自动发送，请勿回复。");

            if (account == null || password == null || host == null) {
                logger.error("邮件配置不完整：account={}, password={}, host={}", 
                    account != null ? "已配置" : "未配置", 
                    password != null ? "已配置" : "未配置", 
                    host != null ? "已配置" : "未配置");
                return null;
            }

            EmailConfig emailConfig = new EmailConfig();
            emailConfig.setAccount(account);
            emailConfig.setPassword(password);
            emailConfig.setHost(host);
            emailConfig.setPort(Integer.parseInt(port));
            emailConfig.setSubject(subject);
            emailConfig.setContent(content);

            logger.info("邮件配置获取成功：host={}, port={}, account={}", host, port, account);
            return emailConfig;

        } catch (Exception e) {
            logger.error("获取邮件配置异常：{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取所有配置（管理员专用）
     */
    public List<Config> getAllConfigs() {
        try {
            List<Config> allConfigs = configRepository.findAll();
            logger.info("获取所有配置成功，共 {} 条", allConfigs.size());
            return allConfigs;
        } catch (Exception e) {
            logger.error("获取所有配置异常：{}", e.getMessage(), e);
            return List.of();
        }
    }

    /**
     * 获取公开配置列表
     */
    public List<Config> getPublicConfigs() {
        try {
            List<Config> publicConfigs = configRepository.findPublicConfigs();
            logger.info("获取公开配置成功，共 {} 条", publicConfigs.size());
            return publicConfigs;
        } catch (Exception e) {
            logger.error("获取公开配置异常：{}", e.getMessage(), e);
            return List.of();
        }
    }

    /**
     * 根据配置键获取公开配置
     */
    public Config getPublicConfigByKey(String configKey) {
        try {
            Optional<Config> configOpt = configRepository.findPublicConfigByKey(configKey);
            if (configOpt.isPresent()) {
                logger.info("获取公开配置成功：{}", configKey);
                return configOpt.get();
            } else {
                logger.warn("公开配置项不存在：{}", configKey);
                return null;
            }
        } catch (Exception e) {
            logger.error("获取公开配置异常：{}，错误信息：{}", configKey, e.getMessage(), e);
            return null;
        }
    }

    /**
     * 邮件配置类
     */
    public static class EmailConfig {
        private String account;
        private String password;
        private String host;
        private Integer port;
        private String subject;
        private String content;

        // Getter 和 Setter 方法
        public String getAccount() { return account; }
        public void setAccount(String account) { this.account = account; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getHost() { return host; }
        public void setHost(String host) { this.host = host; }
        
        public Integer getPort() { return port; }
        public void setPort(Integer port) { this.port = port; }
        
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}