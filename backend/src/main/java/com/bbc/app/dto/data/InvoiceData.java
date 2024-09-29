package com.bbc.app.dto.data;

import com.bbc.app.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class InvoiceData {
    private UUID invoiceId;
    private BigDecimal unitConsumption;
    private String billDuration;
    private LocalDate billDueDate;
    private BigDecimal amountDue;
    private PaymentStatus paymentStatus;

    public InvoiceData(UUID invoiceId, BigDecimal unitConsumption, String billDuration, LocalDate billDueDate, BigDecimal amountDue, PaymentStatus paymentStatus) {
        this.invoiceId = invoiceId;
        this.unitConsumption = unitConsumption;
        this.billDuration = billDuration;
        this.billDueDate = billDueDate;
        this.amountDue = amountDue;
        this.paymentStatus = paymentStatus;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public BigDecimal getUnitConsumption() {
        return unitConsumption;
    }

    public void setUnitConsumption(BigDecimal unitConsumption) {
        this.unitConsumption = unitConsumption;
    }

    public String getBillDuration() {
        return billDuration;
    }

    public void setBillDuration(String billDuration) {
        this.billDuration = billDuration;
    }

    public LocalDate getBillDueDate() {
        return billDueDate;
    }

    public void setBillDueDate(LocalDate billDueDate) {
        this.billDueDate = billDueDate;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
