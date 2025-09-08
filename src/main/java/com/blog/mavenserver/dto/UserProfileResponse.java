package com.blog.mavenserver.dto;

public class UserProfileResponse {
    
    private Boolean success;
    private String message;
    private UserProfileInfo userProfile;
    
    public UserProfileResponse() {}
    
    public UserProfileResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public UserProfileResponse(Boolean success, String message, UserProfileInfo userProfile) {
        this.success = success;
        this.message = message;
        this.userProfile = userProfile;
    }
    
    // Getters and Setters
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
    
    public UserProfileInfo getUserProfile() {
        return userProfile;
    }
    
    public void setUserProfile(UserProfileInfo userProfile) {
        this.userProfile = userProfile;
    }
}