package com.bbc.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonIgnore(value = true)
    private UUID invoiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meter_no", nullable = false, referencedColumnName = "meter_no")
    private Customer customer;

    @Column(name = "unit_consumption", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitConsumption;

    @Column(name = "bill_duration", length = 50)
    private String billDuration; // e.g., 'January 2024'

    @Column(name = "bill_due_date", nullable = false)
    private LocalDate billDueDate;

    @Column(name = "current_amount_due", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentAmountDue;

    @Column(name = "total_amount_due", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmountDue;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 10)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;

    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    @Column(name = "invoice_pdf", columnDefinition = "LONGBLOB")
    private byte[] invoicePdf;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PaymentTransaction> paymentTransactions;

    @PrePersist
    protected void onCreate() {
        this.generatedAt = LocalDateTime.now();
    }

    public Invoice() {
    }

    public Invoice(UUID invoiceId, Customer customer, BigDecimal unitConsumption, String billDuration, LocalDate billDueDate, BigDecimal currentAmountDue, BigDecimal totalAmountDue, PaymentStatus paymentStatus, LocalDateTime generatedAt, byte[] invoicePdf, Set<PaymentTransaction> paymentTransactions) {
        this.invoiceId = invoiceId;
        this.customer = customer;
        this.unitConsumption = unitConsumption;
        this.billDuration = billDuration;
        this.billDueDate = billDueDate;
        this.currentAmountDue = currentAmountDue;
        this.totalAmountDue = totalAmountDue;
        this.paymentStatus = paymentStatus;
        this.generatedAt = generatedAt;
        this.invoicePdf = invoicePdf;
        this.paymentTransactions = paymentTransactions;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public Set<PaymentTransaction> getPaymentTransactions() {
        return paymentTransactions;
    }

    public void setPaymentTransactions(Set<PaymentTransaction> paymentTransactions) {
        this.paymentTransactions = paymentTransactions;
    }

    public boolean isValid() {
        return customer != null && customer.getUser().isValid()
                && unitConsumption != null && unitConsumption.compareTo(BigDecimal.ZERO) > 0
                && billDuration != null && !billDuration.trim().isEmpty()
                && billDueDate != null
                && currentAmountDue != null && currentAmountDue.compareTo(BigDecimal.ZERO) >= 0
                && totalAmountDue != null && totalAmountDue.compareTo(BigDecimal.ZERO) >= 0
                && paymentStatus != null;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", customer=" + customer +
                ", unitConsumption=" + unitConsumption +
                ", billDuration='" + billDuration + '\'' +
                ", billDueDate=" + billDueDate +
                ", currentAmountDue=" + currentAmountDue +
                ", totalAmountDue=" + totalAmountDue +
                ", paymentStatus=" + paymentStatus +
                ", generatedAt=" + generatedAt +
                ", invoicePdf=" + Arrays.toString(invoicePdf) +
                ", paymentTransaction=" + paymentTransactions +
                '}';
    }
}
