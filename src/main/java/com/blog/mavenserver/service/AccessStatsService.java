package com.blog.mavenserver.service;

import com.blog.mavenserver.dto.AccessStatsItem;
import com.blog.mavenserver.entity.AccessStats;
import com.blog.mavenserver.entity.TotalAccessStats;
import com.blog.mavenserver.repository.AccessStatsRepository;
import com.blog.mavenserver.repository.TotalAccessStatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccessStatsService {
    
    private static final Logger logger = LoggerFactory.getLogger(AccessStatsService.class);
    
    @Autowired
    private AccessStatsRepository accessStatsRepository;
    
    @Autowired
    private TotalAccessStatsRepository totalAccessStatsRepository;
    
    public void recordAccess(String ip) {
        try {
            // 查找或创建IP访问记录
            Optional<AccessStats> existingStats = accessStatsRepository.findByIp(ip);
            AccessStats stats;
            
            if (existingStats.isPresent()) {
                stats = existingStats.get();
                stats.setVisitCount(stats.getVisitCount() + 1);
                stats.setTotalCount(stats.getTotalCount() + 1);
            } else {
                stats = new AccessStats(ip);
                stats.setVisitCount(1);
                stats.setTotalCount(1);
            }
            
            accessStatsRepository.save(stats);
            
            // 更新总访问次数
            updateTotalAccessCount();
            
            logger.info("记录访问：IP {} 访问次数 {}", ip, stats.getVisitCount());
            
        } catch (Exception e) {
            logger.error("记录访问失败：IP {}, 错误：{}", ip, e.getMessage(), e);
        }
    }
    
    public Long getTodayVisitCount() {
        try {
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
            
            return accessStatsRepository.countTodayVisits(startOfDay, endOfDay);
        } catch (Exception e) {
            logger.error("获取今日访问量失败：{}", e.getMessage(), e);
            return 0L;
        }
    }
    
    public Long getIpTotalVisitCount(String ip) {
        try {
            Optional<AccessStats> stats = accessStatsRepository.findByIp(ip);
            return stats.map(accessStats -> accessStats.getTotalCount().longValue()).orElse(0L);
        } catch (Exception e) {
            logger.error("获取IP总访问量失败：IP {}, 错误：{}", ip, e.getMessage(), e);
            return 0L;
        }
    }
    
    public Long getTotalVisitCount() {
        try {
            Optional<TotalAccessStats> totalStats = totalAccessStatsRepository.findFirst();
            return totalStats.map(TotalAccessStats::getTotalCount).orElse(0L);
        } catch (Exception e) {
            logger.error("获取总访问量失败：{}", e.getMessage(), e);
            return 0L;
        }
    }
    
    public Long getTotalTodayVisitCount() {
        try {
            Optional<TotalAccessStats> totalStats = totalAccessStatsRepository.findFirst();
            return totalStats.map(TotalAccessStats::getTodayCount).orElse(0L);
        } catch (Exception e) {
            logger.error("获取今日总访问量失败：{}", e.getMessage(), e);
            return 0L;
        }
    }
    
    public List<AccessStatsItem> getAllAccessStatsList() {
        try {
            // 按总访问次数降序排列
            List<AccessStats> accessStatsList = accessStatsRepository.findAll(
                Sort.by(Sort.Direction.DESC, "totalCount")
            );
            
            return accessStatsList.stream()
                .map(stats -> new AccessStatsItem(
                    stats.getId(),
                    stats.getIp(),
                    stats.getVisitCount(),
                    stats.getTotalCount(),
                    stats.getCreatedTime(),
                    stats.getUpdatedTime()
                ))
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            logger.error("获取访问统计列表失败：{}", e.getMessage(), e);
            return List.of();
        }
    }
    
    public Long getTotalIpCount() {
        try {
            return accessStatsRepository.count();
        } catch (Exception e) {
            logger.error("获取总IP数量失败：{}", e.getMessage(), e);
            return 0L;
        }
    }
    
    private void updateTotalAccessCount() {
        try {
            Long totalSum = accessStatsRepository.sumAllTotalCounts();
            
            LocalDate today = LocalDate.now();
            LocalDateTime startOfDay = today.atStartOfDay();
            LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();
            Long todaySum = accessStatsRepository.sumTodayVisitCounts(startOfDay, endOfDay);
            
            Optional<TotalAccessStats> totalStatsOpt = totalAccessStatsRepository.findFirst();
            TotalAccessStats totalStats;
            
            if (totalStatsOpt.isPresent()) {
                totalStats = totalStatsOpt.get();
                totalStats.setTotalCount(totalSum);
                totalStats.setTodayCount(todaySum);
            } else {
                totalStats = new TotalAccessStats(totalSum, todaySum);
            }
            
            totalAccessStatsRepository.save(totalStats);
            
        } catch (Exception e) {
            logger.error("更新总访问次数失败：{}", e.getMessage(), e);
        }
    }
}