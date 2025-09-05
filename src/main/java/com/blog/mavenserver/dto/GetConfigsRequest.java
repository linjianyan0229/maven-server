package com.blog.mavenserver.dto;

public class GetConfigsRequest {
    
    private String token; // Token值，用于身份验证

    public GetConfigsRequest() {}

    public GetConfigsRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}