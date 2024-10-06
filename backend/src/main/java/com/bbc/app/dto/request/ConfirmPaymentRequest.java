package com.bbc.app.dto.request;

import com.bbc.app.model.PaymentMethod;

import java.math.BigDecimal;
import java.util.UUID;

public class ConfirmPaymentRequest {
    private String meterNo;
    private UUID invoiceId;
    private PaymentMethod paymentMethod;
    private String paymentDetails;
    private String otp;
    private BigDecimal amount;

    public ConfirmPaymentRequest(String meterNo, UUID invoiceId, PaymentMethod paymentMethod, String paymentDetails, String otp, BigDecimal amount) {
        this.meterNo = meterNo;
        this.invoiceId = invoiceId;
        this.paymentMethod = paymentMethod;
        this.paymentDetails = paymentDetails;
        this.otp = otp;
        this.amount = amount;
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

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}