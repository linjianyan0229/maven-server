package com.blog.mavenserver.dto;

public class RegisterRequest {
    
    private String email; // 邮箱
    private String code; // 验证码
    private String password; // 密码
    private String confirmPassword; // 确认密码

    public RegisterRequest() {}

    public RegisterRequest(String email, String code, String password, String confirmPassword) {
        this.email = email;
        this.code = code;
        this.password = password;
        this.confirmPassword = confirmPassword;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}