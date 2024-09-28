package com.bbc.app.dto.response;

public class LoginResponse {
    private String message;
    private String token;
    private String role;
    private boolean success;

    public LoginResponse(String message, String token, String role, boolean success) {
        this.message = message;
        this.token = token;
        this.role = role;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

