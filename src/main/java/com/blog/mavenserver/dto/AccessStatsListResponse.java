package com.blog.mavenserver.dto;

import java.util.List;

public class AccessStatsListResponse {
    
    private Boolean success;
    private String message;
    private List<AccessStatsItem> accessStatsList;
    private Long totalCount;
    private Long totalIpCount;
    
    public AccessStatsListResponse() {}
    
    public AccessStatsListResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public AccessStatsListResponse(Boolean success, String message, 
                                 List<AccessStatsItem> accessStatsList, 
                                 Long totalCount, Long totalIpCount) {
        this.success = success;
        this.message = message;
        this.accessStatsList = accessStatsList;
        this.totalCount = totalCount;
        this.totalIpCount = totalIpCount;
    }
    
    // Getters and Setters
    public Boolean getSuccess() {
        return success;
    }
    
    public void setSuccess(Boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public List<AccessStatsItem> getAccessStatsList() {
        return accessStatsList;
    }
    
    public void setAccessStatsList(List<AccessStatsItem> accessStatsList) {
        this.accessStatsList = accessStatsList;
    }
    
    public Long getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
    
    public Long getTotalIpCount() {
        return totalIpCount;
    }
    
    public void setTotalIpCount(Long totalIpCount) {
        this.totalIpCount = totalIpCount;
    }
}