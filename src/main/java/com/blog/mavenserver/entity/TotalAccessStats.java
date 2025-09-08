package com.blog.mavenserver.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "total_access_stats")
public class TotalAccessStats {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "total_count", columnDefinition = "bigint default 0")
    private Long totalCount = 0L;
    
    @Column(name = "today_count", columnDefinition = "bigint default 0")
    private Long todayCount = 0L;
    
    @Column(name = "updated_time", columnDefinition = "datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedTime;
    
    public TotalAccessStats() {}
    
    public TotalAccessStats(Long totalCount, Long todayCount) {
        this.totalCount = totalCount;
        this.todayCount = todayCount;
        this.updatedTime = LocalDateTime.now();
    }
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
    
    public Long getTodayCount() {
        return todayCount;
    }
    
    public void setTodayCount(Long todayCount) {
        this.todayCount = todayCount;
    }
    
    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }
    
    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}