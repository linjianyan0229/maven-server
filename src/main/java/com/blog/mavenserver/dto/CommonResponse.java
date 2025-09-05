package com.blog.mavenserver.dto;

public class CommonResponse {
    
    private Boolean success; // 是否成功
    private String message; // 返回消息

    public CommonResponse() {}

    public CommonResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
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
}