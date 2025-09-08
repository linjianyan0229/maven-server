package com.blog.mavenserver.dto;

public class AccessStatsResponse {
    
    private Boolean success;
    private String message;
    private String ip;
    private Long todayVisits;
    private Long ipTotalVisits;
    
    public AccessStatsResponse() {}
    
    public AccessStatsResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public AccessStatsResponse(Boolean success, String message, String ip, Long todayVisits, Long ipTotalVisits) {
        this.success = success;
        this.message = message;
        this.ip = ip;
        this.todayVisits = todayVisits;
        this.ipTotalVisits = ipTotalVisits;
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
    
    public String getIp() {
        return ip;
    }
    
    public void setIp(String ip) {
        this.ip = ip;
    }
    
    public Long getTodayVisits() {
        return todayVisits;
    }
    
    public void setTodayVisits(Long todayVisits) {
        this.todayVisits = todayVisits;
    }
    
    public Long getIpTotalVisits() {
        return ipTotalVisits;
    }
    
    public void setIpTotalVisits(Long ipTotalVisits) {
        this.ipTotalVisits = ipTotalVisits;
    }
}