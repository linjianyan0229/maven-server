package com.blog.mavenserver.dto;

public class TokenValidateRequest {
    
    private String token; // Token值

    public TokenValidateRequest() {}

    public TokenValidateRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}