package com.blog.mavenserver.dto;

public class DeleteUserRequest {
    private String token;
    private Long userId;

    public DeleteUserRequest() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}