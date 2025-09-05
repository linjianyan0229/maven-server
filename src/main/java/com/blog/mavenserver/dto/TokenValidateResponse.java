package com.blog.mavenserver.dto;

import com.blog.mavenserver.entity.User;

public class TokenValidateResponse {
    
    private Boolean success; // 验证是否成功
    private String message; // 返回消息
    private Boolean valid; // Token是否有效
    private UserInfo userInfo; // 用户信息

    public TokenValidateResponse() {}

    public TokenValidateResponse(Boolean success, String message, Boolean valid) {
        this.success = success;
        this.message = message;
        this.valid = valid;
    }

    public TokenValidateResponse(Boolean success, String message, Boolean valid, User user) {
        this.success = success;
        this.message = message;
        this.valid = valid;
        if (user != null) {
            this.userInfo = new UserInfo(user);
        }
    }

    // 内部类：用户信息
    public static class UserInfo {
        private Long id;
        private String username;
        private String email;
        private String avatar;
        private Integer sex;
        private String bio;
        private String role;
        private String status;

        public UserInfo(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.avatar = user.getAvatar();
            this.sex = user.getSex();
            this.bio = user.getBio();
            this.role = user.getRole();
            this.status = user.getStatus();
        }

        // Getter 方法
        public Long getId() { return id; }
        public String getUsername() { return username; }
        public String getEmail() { return email; }
        public String getAvatar() { return avatar; }
        public Integer getSex() { return sex; }
        public String getBio() { return bio; }
        public String getRole() { return role; }
        public String getStatus() { return status; }
    }

    // Getter 和 Setter 方法
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

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}