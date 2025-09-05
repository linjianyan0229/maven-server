package com.blog.mavenserver.dto;

import com.blog.mavenserver.entity.User;
import java.time.LocalDateTime;

public class LoginResponse {
    
    private Boolean success; // 登录是否成功
    private String message; // 返回消息
    private String token; // Token
    private LocalDateTime expireTime; // Token过期时间
    private UserInfo userInfo; // 用户信息

    public LoginResponse() {}

    public LoginResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public LoginResponse(Boolean success, String message, String token, LocalDateTime expireTime, User user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.expireTime = expireTime;
        this.userInfo = new UserInfo(user);
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}