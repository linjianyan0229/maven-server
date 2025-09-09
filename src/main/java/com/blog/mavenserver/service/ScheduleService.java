package com.blog.mavenserver.service;

import com.blog.mavenserver.entity.AccessStats;
import com.blog.mavenserver.entity.TotalAccessStats;
import com.blog.mavenserver.repository.AccessStatsRepository;
import com.blog.mavenserver.repository.TotalAccessStatsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ScheduleService {
    
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);
    
    @Autowired
    private AccessStatsRepository accessStatsRepository;
    
    @Autowired
    private TotalAccessStatsRepository totalAccessStatsRepository;
    
    /**
     * 每日凌晨0点重置今日访问统计
     * cron表达式：秒 分 时 日 月 星期
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyAccessStats() {
        try {
            logger.info("开始执行每日访问统计重置任务");
            
            // 重置所有IP的今日访问次数（visit_count）
            List<AccessStats> allStats = accessStatsRepository.findAll();
            int resetCount = 0;
            
            for (AccessStats stats : allStats) {
                if (stats.getVisitCount() > 0) {
                    stats.setVisitCount(0);
                    accessStatsRepository.save(stats);
                    resetCount++;
                }
            }
            
            // 重置总访问统计中的今日计数
            Optional<TotalAccessStats> totalStatsOpt = totalAccessStatsRepository.findFirst();
            if (totalStatsOpt.isPresent()) {
                TotalAccessStats totalStats = totalStatsOpt.get();
                totalStats.setTodayCount(0L);
                totalAccessStatsRepository.save(totalStats);
            }
            
            logger.info("每日访问统计重置任务完成：重置了 {} 个IP的今日访问量，总今日访问量已重置", resetCount);
            
        } catch (Exception e) {
            logger.error("每日访问统计重置任务失败：{}", e.getMessage(), e);
        }
    }
    
    /**
     * 每小时同步总访问统计数据
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void syncTotalAccessStats() {
        try {
            logger.info("开始执行总访问统计同步任务");
            
            Long totalSum = accessStatsRepository.sumAllTotalCounts();
            Long todaySum = accessStatsRepository.sumAllVisitCounts();
            
            Optional<TotalAccessStats> totalStatsOpt = totalAccessStatsRepository.findFirst();
            TotalAccessStats totalStats;
            
            if (totalStatsOpt.isPresent()) {
                totalStats = totalStatsOpt.get();
                totalStats.setTotalCount(totalSum != null ? totalSum : 0L);
                totalStats.setTodayCount(todaySum != null ? todaySum : 0L);
            } else {
                totalStats = new TotalAccessStats(
                    totalSum != null ? totalSum : 0L, 
                    todaySum != null ? todaySum : 0L
                );
            }
            
            totalAccessStatsRepository.save(totalStats);
            
            logger.info("总访问统计同步任务完成：总访问量 {}，今日访问量 {}", totalSum, todaySum);
            
        } catch (Exception e) {
            logger.error("总访问统计同步任务失败：{}", e.getMessage(), e);
        }
    }
}