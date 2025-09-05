package com.blog.mavenserver.dto;

import com.blog.mavenserver.entity.Config;
import java.util.List;

public class GetPublicConfigsResponse {
    
    private Boolean success;
    private String message;
    private List<Config> configs;
    
    public GetPublicConfigsResponse() {}
    
    public GetPublicConfigsResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public GetPublicConfigsResponse(Boolean success, String message, List<Config> configs) {
        this.success = success;
        this.message = message;
        this.configs = configs;
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
    
    public List<Config> getConfigs() {
        return configs;
    }
    
    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }
}