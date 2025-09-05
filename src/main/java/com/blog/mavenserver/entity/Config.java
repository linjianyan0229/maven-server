package com.blog.mavenserver.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "config")
public class Config {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String configKey;
    
    @Column(length = 255)
    private String description;
    
    @Column(name = "config_value", nullable = false, columnDefinition = "text")
    private String configValue;
    
    @Column(length = 10, columnDefinition = "varchar(10) default '公开'")
    private String status;

    // 构造函数
    public Config() {}
    
    public Config(String configKey, String description, String configValue, String status) {
        this.configKey = configKey;
        this.description = description;
        this.configValue = configValue;
        this.status = status;
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}