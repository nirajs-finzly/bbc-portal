package com.bbc.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Random;
import java.util.Set;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @Column(name = "customer_id", length = 15, unique = true)
    @JsonIgnore(value = true)
    private String customerId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "meter_no", unique = true, length = 50)
    @JsonIgnore(value = true)
    private String meterNo;

    @Column(length = 255)
    private String address;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Invoice> invoices;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PaymentTransaction> paymentTransactions;

    public Customer() {
    }

    public Customer(String customerId, User user, String meterNo, String address, Set<Invoice> invoices, Set<PaymentTransaction> paymentTransactions) {
        this.customerId = customerId;
        this.user = user;
        this.meterNo = meterNo;
        this.address = address;
        this.invoices = invoices;
        this.paymentTransactions = paymentTransactions;
    }

    @PrePersist
    private void generateCustomerIdAndMeterNo() {
        if (this.customerId == null || this.customerId.isEmpty()) {
            this.customerId = "CRN" + generateRandomNumber();
        }
        if (this.meterNo == null || this.meterNo.isEmpty()) {
            this.meterNo = "MTR" + generateRandomNumber();
        }
    }

    private String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000000) + 1000000; // Ensures 7 digits
        return String.valueOf(randomNumber);
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(Set<Invoice> invoices) {
        this.invoices = invoices;
    }

    public Set<PaymentTransaction> getPaymentTransactions() {
        return paymentTransactions;
    }

    public void setPaymentTransactions(Set<PaymentTransaction> paymentTransactions) {
        this.paymentTransactions = paymentTransactions;
    }



}
