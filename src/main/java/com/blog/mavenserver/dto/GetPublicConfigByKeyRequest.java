package com.blog.mavenserver.dto;

public class GetPublicConfigByKeyRequest {
    
    private String configKey;
    
    public GetPublicConfigByKeyRequest() {}
    
    public GetPublicConfigByKeyRequest(String configKey) {
        this.configKey = configKey;
    }
    
    public String getConfigKey() {
        return configKey;
    }
    
    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }
}