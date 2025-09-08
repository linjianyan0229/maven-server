package com.blog.mavenserver.dto;

public class UserProfileRequest {
    
    private String token;
    
    public UserProfileRequest() {}
    
    public UserProfileRequest(String token) {
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