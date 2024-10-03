package com.bbc.app.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "debit_cards")
public class DebitCard {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cardId;

    @Column(name = "card_number", nullable = false, unique = true, length = 16)
    private String cardNumber;

    @Column(name = "expiry_date", nullable = false, length = 5) // Format MM/YY
    private String expiryDate;

    @Column(name = "cvv", nullable = false, length = 3)
    private String cvv;

    public DebitCard() {}

    public UUID getCardId() {
        return cardId;
    }

    public void setCardId(UUID cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
