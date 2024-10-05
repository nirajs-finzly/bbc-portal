package com.bbc.app.repository;

import com.bbc.app.model.PaymentTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentTransactionRepsitory extends JpaRepository<PaymentTransaction, UUID> {

    Page<PaymentTransaction> findAllByCustomerCustomerId(String customerId, Pageable pageable);
}
