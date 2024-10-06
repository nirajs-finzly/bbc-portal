package com.bbc.app.controller;

import com.bbc.app.dto.request.CashPaymentRequest;
import com.bbc.app.dto.request.ConfirmPaymentRequest;
import com.bbc.app.dto.request.InitiatePaymentRequest;
import com.bbc.app.dto.request.MakePaymentRequest;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.PaymentResponse;
import com.bbc.app.dto.response.TransactionsResponse;
import com.bbc.app.service.PaymentService;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping("/transactions/{meterNo}")
    public ResponseEntity<TransactionsResponse> getAllTransactionsByMeterNo(
            @PathVariable @Pattern(regexp = "^(MTR)\\d{7}$", message = "Invalid meter no.") String meterNo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return paymentService.getAllTransactionsByMeterNo(meterNo, page, size);
    }

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(@RequestBody InitiatePaymentRequest request) {
        return paymentService.initiatePayment(request.getMeterNo(), request.getInvoiceId());
    }

    @GetMapping("/validate-initiation/{invoiceId}")
    public ResponseEntity<MessageResponse> validatePaymentInitiation(@PathVariable UUID invoiceId) {
        return paymentService.validatePaymentInitiation(invoiceId);
    }

    @PostMapping("/make-payment")
    public ResponseEntity<MessageResponse> makePayment(@RequestBody MakePaymentRequest request) {
        return paymentService.makePayment(request.getMeterNo(), request.getInvoiceId(), request.getPaymentMethod(), request.getPaymentDetails());
    }

    @PostMapping("/confirm")
    public ResponseEntity<PaymentResponse> confirmPayment(@RequestBody ConfirmPaymentRequest request) {
        return paymentService.confirmPayment(request.getMeterNo(), request.getInvoiceId(), request.getPaymentMethod(), request.getPaymentDetails(), request.getOtp(), request.getAmount());
    }

    @PostMapping("/cash-payment")
    public ResponseEntity<MessageResponse> initiatePayment(@RequestBody CashPaymentRequest request) {
        return paymentService.markInvoiceAsPaid(request.getInvoiceId(), request.getPaymentMethod());
    }
}
