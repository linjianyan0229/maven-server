package com.blog.mavenserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class StartupConfig {

    private static final Logger logger = LoggerFactory.getLogger(StartupConfig.class);

    @Autowired
    private Environment environment;

    @Autowired
    private DataSource dataSource;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        String port = environment.getProperty("server.port", "8080");
        String dbUrl = environment.getProperty("spring.datasource.url");
        
        logger.info("=================================================");
        logger.info("🚀 应用启动成功");
        logger.info("📱 服务器运行地址: http://localhost:{}", port);
        
        // Test database connection
        try (Connection connection = dataSource.getConnection()) {
            logger.info("✅ 数据库连接成功: {}", dbUrl);
        } catch (Exception e) {
            logger.error("❌ 数据库连接失败: {}", e.getMessage());
        }
        
        logger.info("=================================================");
    }
}