package com.bbc.app.controller;

import com.bbc.app.dto.request.ConfirmPaymentRequest;
import com.bbc.app.dto.request.InitiatePaymentRequest;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<MessageResponse> initiatePayment(@RequestBody InitiatePaymentRequest request) {
        return paymentService.initiatePayment(request.getCustomerId(), request.getInvoiceId(), request.getPaymentMethod(), request.getPaymentDetails());
    }

    @PostMapping("/confirm")
    public ResponseEntity<MessageResponse> confirmPayment(@RequestBody ConfirmPaymentRequest request) {
        return paymentService.confirmPayment(request.getCustomerId(), request.getInvoiceId(), request.getPaymentMethod(), request.getOtp(), request.getAmount());
    }
}
