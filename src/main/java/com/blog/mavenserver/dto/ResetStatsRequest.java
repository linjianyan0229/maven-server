package com.blog.mavenserver.dto;

public class ResetStatsRequest {
    private String token;
    private String resetType; // "daily" - 重置今日统计, "all" - 重置所有统计

    public ResetStatsRequest() {}

    public ResetStatsRequest(String token, String resetType) {
        this.token = token;
        this.resetType = resetType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getResetType() {
        return resetType;
    }

    public void setResetType(String resetType) {
        this.resetType = resetType;
    }
}