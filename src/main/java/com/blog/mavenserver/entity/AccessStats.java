package com.blog.mavenserver.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "access_stats")
public class AccessStats {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "ip", nullable = false, length = 45, unique = true)
    private String ip;
    
    @Column(name = "visit_count", columnDefinition = "int default 0")
    private Integer visitCount = 0;
    
    @Column(name = "total_count", columnDefinition = "int default 0")
    private Integer totalCount = 0;
    
    @Column(name = "created_time", columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private LocalDateTime createdTime;
    
    @Column(name = "updated_time", columnDefinition = "datetime default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updatedTime;
    
    public AccessStats() {}
    
    public AccessStats(String ip) {
        this.ip = ip;
        this.visitCount = 0;
        this.totalCount = 0;
        this.createdTime = LocalDateTime.now();
        this.updatedTime = LocalDateTime.now();
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdTime == null) {
            createdTime = LocalDateTime.now();
        }
        if (updatedTime == null) {
            updatedTime = LocalDateTime.now();
        }
    }
    
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
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public Integer getVisitCount() {
        return visitCount;
    }
    
    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }
    
    public Integer getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
    
    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }
    
    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}