package com.blog.mavenserver.dto;

import java.util.List;

public class UserListResponse {
    private Boolean success;
    private String message;
    private List<UserInfo> users;
    private Long totalCount;

    public UserListResponse() {}

    public UserListResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public UserListResponse(Boolean success, String message, List<UserInfo> users, Long totalCount) {
        this.success = success;
        this.message = message;
        this.users = users;
        this.totalCount = totalCount;
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

    public List<UserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}