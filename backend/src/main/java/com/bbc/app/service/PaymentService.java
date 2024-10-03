package com.bbc.app.service;

import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.model.PaymentMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    ResponseEntity<MessageResponse> initiatePayment(String customerId, UUID invoiceId, PaymentMethod paymentMethod, String paymentDetails);

    ResponseEntity<MessageResponse> confirmPayment(String customerId, UUID invoiceId, PaymentMethod paymentMethod, String otp, BigDecimal amount);
}
