package com.bbc.app.repository;

import com.bbc.app.dto.data.InvoiceData;
import com.bbc.app.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    // Fetching invoices based on the meter number of the customer
    List<InvoiceData> findByCustomerMeterNo(String meterNo);

    // Find unpaid Invoices for a Customer
    List<Invoice> findByCustomer_CustomerIdAndPaymentStatus(String customerId, String paymentStatus);
}
