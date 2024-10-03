package com.bbc.app.dto.request;

import com.bbc.app.model.PaymentMethod;

import java.util.UUID;

public class CashPaymentRequest {
    private UUID invoiceId;
    private PaymentMethod paymentMethod;

    public CashPaymentRequest(UUID invoiceId, PaymentMethod paymentMethod) {
        this.invoiceId = invoiceId;
        this.paymentMethod = paymentMethod;
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
}
