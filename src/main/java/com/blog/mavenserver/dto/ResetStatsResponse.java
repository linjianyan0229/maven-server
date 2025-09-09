package com.blog.mavenserver.dto;

public class ResetStatsResponse {
    private Boolean success;
    private String message;
    private Long resetCount;
    private String resetType;

    public ResetStatsResponse() {}

    public ResetStatsResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ResetStatsResponse(Boolean success, String message, Long resetCount, String resetType) {
        this.success = success;
        this.message = message;
        this.resetCount = resetCount;
        this.resetType = resetType;
    }

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

    public Long getResetCount() {
        return resetCount;
    }

    public void setResetCount(Long resetCount) {
        this.resetCount = resetCount;
    }

    public String getResetType() {
        return resetType;
    }

    public void setResetType(String resetType) {
        this.resetType = resetType;
    }
}