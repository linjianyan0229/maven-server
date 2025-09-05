package com.blog.mavenserver.dto;

public class LoginRequest {
    
    private String login; // 用户名或邮箱
    private String password; // 密码
    private Boolean rememberMe; // 记住我

    public LoginRequest() {}

    public LoginRequest(String login, String password, Boolean rememberMe) {
        this.login = login;
        this.password = password;
        this.rememberMe = rememberMe;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}