package com.blog.mavenserver.dto;

public class LogoutRequest {
    
    private String token; // Token值

    public LogoutRequest() {}

    public LogoutRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}