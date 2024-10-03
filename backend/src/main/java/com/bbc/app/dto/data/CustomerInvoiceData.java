package com.bbc.app.dto.data;

import com.bbc.app.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CustomerInvoiceData {
    private UUID invoiceId;
    private BigDecimal unitConsumption;
    private String billDuration;
    private LocalDate billDueDate;
    private BigDecimal currentAmountDue;
    private BigDecimal totalAmountDue;
    private PaymentStatus paymentStatus;
    private LocalDateTime generatedAt;
    private byte[] invoicePdf;

    public CustomerInvoiceData(UUID invoiceId, BigDecimal unitConsumption, String billDuration, LocalDate billDueDate, BigDecimal currentAmountDue, BigDecimal totalAmountDue, PaymentStatus paymentStatus, LocalDateTime generatedAt, byte[] invoicePdf) {
        this.invoiceId = invoiceId;
        this.unitConsumption = unitConsumption;
        this.billDuration = billDuration;
        this.billDueDate = billDueDate;
        this.currentAmountDue = currentAmountDue;
        this.totalAmountDue = totalAmountDue;
        this.paymentStatus = paymentStatus;
        this.generatedAt = generatedAt;
        this.invoicePdf = invoicePdf;
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

    public BigDecimal getCurrentAmountDue() {
        return currentAmountDue;
    }

    public void setCurrentAmountDue(BigDecimal currentAmountDue) {
        this.currentAmountDue = currentAmountDue;
    }

    public BigDecimal getTotalAmountDue() {
        return totalAmountDue;
    }

    public void setTotalAmountDue(BigDecimal totalAmountDue) {
        this.totalAmountDue = totalAmountDue;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public byte[] getInvoicePdf() {
        return invoicePdf;
    }

    public void setInvoicePdf(byte[] invoicePdf) {
        this.invoicePdf = invoicePdf;
    }

    @Override
    public String toString() {
        return "InvoiceData{" +
                "invoiceId=" + invoiceId +
                ", unitConsumption=" + unitConsumption +
                ", billDuration='" + billDuration + '\'' +
                ", billDueDate=" + billDueDate +
                ", currentAmountDue=" + currentAmountDue +
                ", totalAmountDue=" + totalAmountDue +
                ", paymentStatus=" + paymentStatus +
                ", generatedAt=" + generatedAt +
                '}';
    }
}
