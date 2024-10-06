package com.bbc.app.dto.data;

import com.bbc.app.model.PaymentMethod;
import com.bbc.app.model.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionData {
    private UUID transactionId;
    private BigDecimal amount;
    private String customerName;
    private String invoiceId;
    private PaymentMethod paymentMethod;
    private TransactionStatus transactionStatus;
    private LocalDateTime transactionDate;
    private String paymentIdentifier;
    private String cardType;

    public TransactionData(UUID transactionId, BigDecimal amount, String customerName, String invoiceId, PaymentMethod paymentMethod, TransactionStatus transactionStatus, LocalDateTime transactionDate, String paymentIdentifier, String cardType) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.customerName = customerName;
        this.invoiceId = invoiceId;
        this.paymentMethod = paymentMethod;
        this.transactionStatus = transactionStatus;
        this.transactionDate = transactionDate;
        this.paymentIdentifier = paymentIdentifier;
        this.cardType = cardType;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(UUID transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPaymentIdentifier() {
        return paymentIdentifier;
    }

    public void setPaymentIdentifier(String paymentIdentifier) {
        this.paymentIdentifier = paymentIdentifier;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}