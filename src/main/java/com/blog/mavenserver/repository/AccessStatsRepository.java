package com.blog.mavenserver.repository;

import com.blog.mavenserver.entity.AccessStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AccessStatsRepository extends JpaRepository<AccessStats, Long> {
    
    Optional<AccessStats> findByIp(String ip);
    
    @Query("SELECT COUNT(a) FROM AccessStats a WHERE a.createdTime >= :startOfDay AND a.createdTime < :endOfDay")
    Long countTodayVisits(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
    
    @Query("SELECT COALESCE(SUM(a.totalCount), 0) FROM AccessStats a")
    Long sumAllTotalCounts();
    
    @Query("SELECT COALESCE(SUM(a.visitCount), 0) FROM AccessStats a WHERE a.updatedTime >= :startOfDay AND a.updatedTime < :endOfDay")
    Long sumTodayVisitCounts(@Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}