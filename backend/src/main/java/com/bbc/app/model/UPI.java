package com.bbc.app.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "upi")
public class UPI {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID upiId;

    @Column(name = "upi_id_value", nullable = false, unique = true, length = 50)
    private String upiIdValue;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    public UPI() {}

    public UUID getUpiId() {
        return upiId;
    }

    public void setUpiId(UUID upiId) {
        this.upiId = upiId;
    }

    public String getUpiIdValue() {
        return upiIdValue;
    }

    public void setUpiIdValue(String upiIdValue) {
        this.upiIdValue = upiIdValue;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
