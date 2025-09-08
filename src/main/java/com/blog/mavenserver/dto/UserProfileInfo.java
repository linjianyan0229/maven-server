package com.blog.mavenserver.dto;

import java.time.LocalDateTime;

public class UserProfileInfo {
    
    private Long id;
    private String username;
    private String email;
    private String avatar;
    private Integer sex;
    private String bio;
    private String role;
    private String status;
    private LocalDateTime createdTime;
    private LocalDateTime loginTime;
    
    public UserProfileInfo() {}
    
    public UserProfileInfo(Long id, String username, String email, String avatar, 
                          Integer sex, String bio, String role, String status,
                          LocalDateTime createdTime, LocalDateTime loginTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.avatar = avatar;
        this.sex = sex;
        this.bio = bio;
        this.role = role;
        this.status = status;
        this.createdTime = createdTime;
        this.loginTime = loginTime;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public Integer getSex() {
        return sex;
    }
    
    public void setSex(Integer sex) {
        this.sex = sex;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedTime() {
        return createdTime;
    }
    
    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }
    
    public LocalDateTime getLoginTime() {
        return loginTime;
    }
    
    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
}