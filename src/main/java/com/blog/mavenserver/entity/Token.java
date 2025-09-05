package com.blog.mavenserver.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token")
public class Token {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(nullable = false, unique = true, length = 255)
    private String token;
    
    @Column(name = "created_time", columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private LocalDateTime createdTime;
    
    @Column(name = "expire_time", nullable = false)
    private LocalDateTime expireTime;

    // 构造函数
    public Token() {}
    
    public Token(Long userId, String token, LocalDateTime expireTime) {
        this.userId = userId;
        this.token = token;
        this.expireTime = expireTime;
        this.createdTime = LocalDateTime.now();
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}