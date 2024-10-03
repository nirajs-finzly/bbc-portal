package com.bbc.app.repository;

import com.bbc.app.dto.data.InvoiceData;
import com.bbc.app.model.Customer;
import com.bbc.app.model.Invoice;
import com.bbc.app.model.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    // Fetching invoices based on the meter number of the customer
    Page<InvoiceData> findByCustomerMeterNo(String meterNo, Pageable pageable);

    @Query("SELECT COUNT(i) FROM Invoice i WHERE i.customer.meterNo = ?1")
    long countByCustomerMeterNo(String meterNo);

    // Find unpaid Invoices for a Customer
    List<Invoice> findByCustomerMeterNoAndPaymentStatus(String meterNo, PaymentStatus paymentStatus);

    List<Invoice> findByCustomerAndPaymentStatus(Customer customer, PaymentStatus paymentStatus);

    Optional<Invoice> findTopByCustomerMeterNoOrderByGeneratedAtDesc(String meterNo);

    Optional<Invoice> findByBillDuration(String billDuration);

    Optional<Invoice> findByCustomerAndBillDuration(Customer customer, String billDuration);
}
