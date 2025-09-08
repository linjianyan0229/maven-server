package com.blog.mavenserver.dto;

public class UpdateProfileRequest {
    
    private String token;
    private String username;
    private String avatar;
    private Integer sex;
    private String bio;
    
    public UpdateProfileRequest() {}
    
    public UpdateProfileRequest(String token, String username, String avatar, Integer sex, String bio) {
        this.token = token;
        this.username = username;
        this.avatar = avatar;
        this.sex = sex;
        this.bio = bio;
    }
    
    // Getters and Setters
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
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
}