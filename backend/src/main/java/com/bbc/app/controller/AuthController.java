package com.bbc.app.controller;

import com.bbc.app.dto.request.LoginRequest;
import com.bbc.app.dto.request.OtpRequest;
import com.bbc.app.dto.response.LoginResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.service.AuthService;
import com.bbc.app.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final OtpService otpService;

    public AuthController(AuthService authService, OtpService otpService) {
        this.authService = authService;
        this.otpService = otpService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<MessageResponse> sendOtp(@Valid @RequestBody OtpRequest request) {
        return otpService.sendOtp(request.getIdentifier());
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
