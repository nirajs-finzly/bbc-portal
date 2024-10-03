package com.bbc.app.repository;

import com.bbc.app.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CreditCardRepository extends JpaRepository<CreditCard, UUID> {
    boolean existsByCardNumber(String cardNumber);
}
