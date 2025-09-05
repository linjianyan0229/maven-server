package com.blog.mavenserver.controller;

import com.blog.mavenserver.dto.GetPublicConfigsResponse;
import com.blog.mavenserver.dto.GetPublicConfigByKeyRequest;
import com.blog.mavenserver.dto.GetPublicConfigByKeyResponse;
import com.blog.mavenserver.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    private static final Logger logger = LoggerFactory.getLogger(ConfigController.class);
    
    @Autowired
    private ConfigService configService;

    @PostMapping("/public")
    public GetPublicConfigsResponse getPublicConfigs() {
        logger.info("收到获取公开配置请求");
        
        var publicConfigs = configService.getPublicConfigs();
        
        if (publicConfigs.isEmpty()) {
            logger.warn("获取公开配置失败：未找到公开配置项");
            return new GetPublicConfigsResponse(false, "未找到公开配置项");
        }
        
        logger.info("获取公开配置成功：共 {} 条配置", publicConfigs.size());
        return new GetPublicConfigsResponse(true, "获取公开配置成功", publicConfigs);
    }

    @PostMapping("/public/by-key")
    public GetPublicConfigByKeyResponse getPublicConfigByKey(@RequestBody GetPublicConfigByKeyRequest request) {
        logger.info("收到根据Key获取公开配置请求：{}", request.getConfigKey());
        
        if (request.getConfigKey() == null || request.getConfigKey().trim().isEmpty()) {
            logger.warn("获取公开配置失败：配置键为空");
            return new GetPublicConfigByKeyResponse(false, "配置键不能为空");
        }
        
        var config = configService.getPublicConfigByKey(request.getConfigKey().trim());
        
        if (config == null) {
            logger.warn("获取公开配置失败：配置项不存在或不是公开状态：{}", request.getConfigKey());
            return new GetPublicConfigByKeyResponse(false, "配置项不存在或不是公开状态");
        }
        
        logger.info("获取公开配置成功：{}", request.getConfigKey());
        return new GetPublicConfigByKeyResponse(true, "获取配置成功", config);
    }
}