package com.bbc.app.dto.response;

public class UserResponse {
    private String message;
    private UserData user;

    public UserResponse(String message, UserData user) {
        this.message = message;
        this.user = user;
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
}
