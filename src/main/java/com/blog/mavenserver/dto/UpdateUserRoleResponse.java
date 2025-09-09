package com.blog.mavenserver.dto;

public class UpdateUserRoleResponse {
    private Boolean success;
    private String message;
    private UserInfo userInfo;

    public UpdateUserRoleResponse() {}

    public UpdateUserRoleResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public UpdateUserRoleResponse(Boolean success, String message, UserInfo userInfo) {
        this.success = success;
        this.message = message;
        this.userInfo = userInfo;
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

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}