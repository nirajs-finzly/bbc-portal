package com.bbc.app.dto.request;

import com.bbc.app.model.PaymentMethod;

import java.util.UUID;

public class InitiatePaymentRequest {
    private String customerId;
    private UUID invoiceId;
    private PaymentMethod paymentMethod;
    private String paymentDetails;

    public InitiatePaymentRequest(String customerId, UUID invoiceId, PaymentMethod paymentMethod, String paymentDetails) {
        this.customerId = customerId;
        this.invoiceId = invoiceId;
        this.paymentMethod = paymentMethod;
        this.paymentDetails = paymentDetails;
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

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}
