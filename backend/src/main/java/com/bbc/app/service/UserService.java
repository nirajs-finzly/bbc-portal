package com.bbc.app.service;

import com.bbc.app.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<UserResponse> getAuthenticatedUser();
}
