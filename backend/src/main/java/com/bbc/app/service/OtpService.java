package com.bbc.app.service;

import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.model.User;
import org.springframework.http.ResponseEntity;

public interface OtpService {
    ResponseEntity<MessageResponse> sendOtp(String userId);

    boolean validateOtp(String inputOtp, User user);
}
