package com.blog.mavenserver.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_code")
public class VerificationCode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String email;
    
    @Column(nullable = false, length = 6)
    private String code;
    
    @Column(nullable = false, length = 10, columnDefinition = "varchar(10) default '注册'")
    private String type;
    
    @Column(name = "created_time", columnDefinition = "datetime default CURRENT_TIMESTAMP")
    private LocalDateTime createdTime;
    
    @Column(name = "expire_time", nullable = false)
    private LocalDateTime expireTime;

    // 构造函数
    public VerificationCode() {}
    
    public VerificationCode(String email, String code, String type, LocalDateTime expireTime) {
        this.email = email;
        this.code = code;
        this.type = type;
        this.expireTime = expireTime;
        this.createdTime = LocalDateTime.now();
    }
    
    // 兼容旧版本的构造函数（默认为注册类型）
    public VerificationCode(String email, String code, LocalDateTime expireTime) {
        this(email, code, "注册", expireTime);
    }

    // Getter 和 Setter 方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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