package com.bbc.app.dto.request;

import com.bbc.app.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public class ConfirmPaymentRequest {
    private String customerId;
    private UUID invoiceId;
    private PaymentMethod paymentMethod;
    private String otp;
    private BigDecimal amount;

    public ConfirmPaymentRequest(String customerId, UUID invoiceId, PaymentMethod paymentMethod, String otp, BigDecimal amount) {
        this.customerId = customerId;
        this.invoiceId = invoiceId;
        this.paymentMethod = paymentMethod;
        this.otp = otp;
        this.amount = amount;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
