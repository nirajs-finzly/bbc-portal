package com.bbc.app.repository;

import com.bbc.app.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByCustomerId(String customerId);

    Optional<Customer> findByMeterNo(String meterNo);

    Page<Customer> findByMeterNoContaining(String meterNo, Pageable pageable);
}
