package com.blog.mavenserver.dto;

public class SendCodeRequest {
    
    private String email; // 邮箱

    public SendCodeRequest() {}

    public SendCodeRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}