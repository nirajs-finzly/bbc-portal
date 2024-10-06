package com.bbc.app.dto.request;

import com.bbc.app.model.PaymentMethod;

import java.util.UUID;

public class MakePaymentRequest {
    private String meterNo;
    private UUID invoiceId;
    private PaymentMethod paymentMethod;
    private String paymentDetails;

    public MakePaymentRequest(String meterNo, UUID invoiceId, PaymentMethod paymentMethod, String paymentDetails) {
        this.meterNo = meterNo;
        this.invoiceId = invoiceId;
        this.paymentMethod = paymentMethod;
        this.paymentDetails = paymentDetails;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
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