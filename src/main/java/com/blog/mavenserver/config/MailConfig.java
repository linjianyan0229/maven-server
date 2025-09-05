package com.blog.mavenserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        
        // 设置默认配置（会被动态配置覆盖）
        mailSender.setHost("smtp.example.com");
        mailSender.setPort(587);
        mailSender.setUsername("default@example.com");
        mailSender.setPassword("default");
        
        return mailSender;
    }
}