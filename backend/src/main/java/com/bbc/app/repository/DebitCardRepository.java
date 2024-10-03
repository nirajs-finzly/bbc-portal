package com.bbc.app.repository;

import com.bbc.app.model.DebitCard;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DebitCardRepository extends JpaRepository<DebitCard, UUID> {
    boolean existsByCardNumber(String cardNumber);

    DebitCard findTopByOrderByCardIdDesc();
}
