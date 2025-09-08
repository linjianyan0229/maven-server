package com.blog.mavenserver.dto;

import java.time.LocalDateTime;

public class AccessStatsItem {
    
    private Long id;
    private String ip;
    private Integer visitCount;
    private Integer totalCount;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
    
    public AccessStatsItem() {}
    
    public AccessStatsItem(Long id, String ip, Integer visitCount, Integer totalCount, 
                          LocalDateTime createdTime, LocalDateTime updatedTime) {
        this.id = id;
        this.ip = ip;
        this.visitCount = visitCount;
        this.totalCount = totalCount;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
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