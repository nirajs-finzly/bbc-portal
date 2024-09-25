package com.bbc.app.service;

import com.bbc.app.dto.response.MessageResponse;
import org.springframework.http.ResponseEntity;

public interface OtpService {
    ResponseEntity<MessageResponse> sendOtp(String userId);
}
