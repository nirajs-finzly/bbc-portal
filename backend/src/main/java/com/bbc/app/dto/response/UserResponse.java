package com.bbc.app.dto.response;

import com.bbc.app.dto.data.UserData;

public class UserResponse {
    private String message;
    private UserData user;
    private boolean success;

    public UserResponse(String message, UserData user, boolean success) {
        this.message = message;
        this.user = user;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
