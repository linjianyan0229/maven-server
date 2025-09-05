package com.blog.mavenserver.dto;

public class SendResetCodeRequest {
    
    private String email;
    
    public SendResetCodeRequest() {}
    
    public SendResetCodeRequest(String email) {
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}