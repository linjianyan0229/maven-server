package com.blog.mavenserver.dto;

public class TotalStatsResponse {
    
    private Boolean success;
    private String message;
    private Long totalVisits;
    private Long todayTotalVisits;
    
    public TotalStatsResponse() {}
    
    public TotalStatsResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public TotalStatsResponse(Boolean success, String message, Long totalVisits, Long todayTotalVisits) {
        this.success = success;
        this.message = message;
        this.totalVisits = totalVisits;
        this.todayTotalVisits = todayTotalVisits;
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
    
    public Long getTotalVisits() {
        return totalVisits;
    }
    
    public void setTotalVisits(Long totalVisits) {
        this.totalVisits = totalVisits;
    }
    
    public Long getTodayTotalVisits() {
        return todayTotalVisits;
    }
    
    public void setTodayTotalVisits(Long todayTotalVisits) {
        this.todayTotalVisits = todayTotalVisits;
    }
}