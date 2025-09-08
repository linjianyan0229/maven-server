package com.blog.mavenserver.controller;

import com.blog.mavenserver.dto.GetPublicConfigsResponse;
import com.blog.mavenserver.dto.GetPublicConfigByKeyRequest;
import com.blog.mavenserver.dto.GetPublicConfigByKeyResponse;
import com.blog.mavenserver.dto.AccessStatsResponse;
import com.blog.mavenserver.dto.TotalStatsResponse;
import com.blog.mavenserver.service.ConfigService;
import com.blog.mavenserver.service.AccessStatsService;
import com.blog.mavenserver.util.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
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
    
    @Autowired
    private AccessStatsService accessStatsService;

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
    
    @PostMapping("/public/access-stats")
    public AccessStatsResponse getAccessStats(HttpServletRequest request) {
        try {
            // 获取客户端IP
            String clientIp = IpUtils.getClientIp(request);
            
            // 记录访问
            accessStatsService.recordAccess(clientIp);
            
            // 获取今日访问量
            Long todayVisits = accessStatsService.getTodayVisitCount();
            
            // 获取该IP的总访问量
            Long ipTotalVisits = accessStatsService.getIpTotalVisitCount(clientIp);
            
            logger.info("获取访问统计成功：IP {} 今日访问量 {} 总访问量 {}", clientIp, todayVisits, ipTotalVisits);
            
            return new AccessStatsResponse(true, "获取访问统计成功", clientIp, todayVisits, ipTotalVisits);
            
        } catch (Exception e) {
            logger.error("获取访问统计失败：{}", e.getMessage(), e);
            return new AccessStatsResponse(false, "获取访问统计失败");
        }
    }
    
    @PostMapping("/public/total-stats")
    public TotalStatsResponse getTotalStats() {
        try {
            // 获取总访问次数
            Long totalVisits = accessStatsService.getTotalVisitCount();
            
            // 获取今日总访问次数
            Long todayTotalVisits = accessStatsService.getTotalTodayVisitCount();
            
            logger.info("获取总统计成功：总访问量 {} 今日总访问量 {}", totalVisits, todayTotalVisits);
            
            return new TotalStatsResponse(true, "获取总统计成功", totalVisits, todayTotalVisits);
            
        } catch (Exception e) {
            logger.error("获取总统计失败：{}", e.getMessage(), e);
            return new TotalStatsResponse(false, "获取总统计失败");
        }
    }
}