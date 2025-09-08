package com.blog.mavenserver.dto;

public class AccessStatsListRequest {
    
    private String token;
    
    public AccessStatsListRequest() {}
    
    public AccessStatsListRequest(String token) {
        this.token = token;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
}