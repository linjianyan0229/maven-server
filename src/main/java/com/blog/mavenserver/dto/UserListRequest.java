package com.blog.mavenserver.dto;

public class UserListRequest {
    private String token;

    public UserListRequest() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}