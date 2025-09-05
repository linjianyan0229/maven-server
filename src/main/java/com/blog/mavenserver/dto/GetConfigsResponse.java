package com.blog.mavenserver.dto;

import com.blog.mavenserver.entity.Config;

import java.util.List;

public class GetConfigsResponse {
    
    private Boolean success; // 是否成功
    private String message; // 返回消息
    private List<ConfigItem> configs; // 配置列表

    public GetConfigsResponse() {}

    public GetConfigsResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public GetConfigsResponse(Boolean success, String message, List<Config> configList) {
        this.success = success;
        this.message = message;
        if (configList != null) {
            this.configs = configList.stream()
                .map(ConfigItem::new)
                .toList();
        }
    }

    // 内部类：配置项信息
    public static class ConfigItem {
        private Long id;
        private String configKey;
        private String description;
        private String configValue;
        private String status;

        public ConfigItem(Config config) {
            this.id = config.getId();
            this.configKey = config.getConfigKey();
            this.description = config.getDescription();
            this.configValue = config.getConfigValue();
            this.status = config.getStatus();
        }

        // Getter 方法
        public Long getId() { return id; }
        public String getConfigKey() { return configKey; }
        public String getDescription() { return description; }
        public String getConfigValue() { return configValue; }
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

    public List<ConfigItem> getConfigs() {
        return configs;
    }

    public void setConfigs(List<ConfigItem> configs) {
        this.configs = configs;
    }
}