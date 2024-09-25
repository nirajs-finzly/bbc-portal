package com.bbc.app.service;

import com.bbc.app.dto.request.LoginRequest;
import com.bbc.app.dto.response.LoginResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<LoginResponse> login(LoginRequest request);
}
