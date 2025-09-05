package com.blog.mavenserver.dto;

import com.blog.mavenserver.entity.Config;

public class GetPublicConfigByKeyResponse {
    
    private Boolean success;
    private String message;
    private Config config;
    
    public GetPublicConfigByKeyResponse() {}
    
    public GetPublicConfigByKeyResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public GetPublicConfigByKeyResponse(Boolean success, String message, Config config) {
        this.success = success;
        this.message = message;
        this.config = config;
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
    
    public Config getConfig() {
        return config;
    }
    
    public void setConfig(Config config) {
        this.config = config;
    }
}