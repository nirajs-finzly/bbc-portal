package com.bbc.app.controller;

import com.bbc.app.dto.request.CashPaymentRequest;
import com.bbc.app.dto.request.ConfirmPaymentRequest;
import com.bbc.app.dto.request.InitiatePaymentRequest;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.TransactionsResponse;
import com.bbc.app.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/transactions")
    public ResponseEntity<TransactionsResponse> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return paymentService.getAllTransactions(page, size);
    }

    @PostMapping("/initiate")
    public ResponseEntity<MessageResponse> initiatePayment(@RequestBody InitiatePaymentRequest request) {
        return paymentService.initiatePayment(request.getCustomerId(), request.getInvoiceId(), request.getPaymentMethod(), request.getPaymentDetails());
    }

    @PostMapping("/confirm")
    public ResponseEntity<MessageResponse> confirmPayment(@RequestBody ConfirmPaymentRequest request) {
        return paymentService.confirmPayment(request.getCustomerId(), request.getInvoiceId(), request.getPaymentMethod(), request.getPaymentDetails(), request.getOtp(), request.getAmount());
    }

    @PostMapping("/cash-payment")
    public ResponseEntity<MessageResponse> initiatePayment(@RequestBody CashPaymentRequest request) {
        return paymentService.markInvoiceAsPaid(request.getInvoiceId(), request.getPaymentMethod());
    }
}
