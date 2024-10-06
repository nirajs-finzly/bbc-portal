package com.bbc.app.service;

import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.PaymentResponse;
import com.bbc.app.dto.response.TransactionsResponse;
import com.bbc.app.model.PaymentMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {
    ResponseEntity<TransactionsResponse> getAllTransactions(int page, int size);

    ResponseEntity<TransactionsResponse> getAllTransactionsByMeterNo(String meterNo, int page, int size);

    ResponseEntity<PaymentResponse> initiatePayment(String customerId, UUID invoiceId);

    ResponseEntity<MessageResponse> validatePaymentInitiation(UUID invoiceId);

    ResponseEntity<MessageResponse> makePayment(String meterNo, UUID invoiceId, PaymentMethod paymentMethod, String paymentDetails);

    ResponseEntity<PaymentResponse> confirmPayment(String meterNo, UUID invoiceId, PaymentMethod paymentMethod, String paymentDetails, String otp, BigDecimal amount);

    ResponseEntity<MessageResponse> markInvoiceAsPaid(UUID invoiceId, PaymentMethod paymentMethod);
}