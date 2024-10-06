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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    long countByCustomerMeterNo(String meterNo);

    List<Invoice> findByCustomerMeterNoAndPaymentStatus(String meterNo, PaymentStatus paymentStatus);

    List<Invoice> findByCustomerAndPaymentStatus(Customer customer, PaymentStatus paymentStatus);

    Optional<Invoice> findTopByCustomerMeterNoOrderByGeneratedAtDesc(String meterNo);

    Optional<Invoice> findByCustomerAndBillDuration(Customer customer, String billDuration);

    @Query("SELECT new com.bbc.app.dto.data.InvoiceData(i.invoiceId, c.user.name, c.meterNo, c.user.name, i.unitConsumption, i.billDuration, i.billDueDate, i.currentAmountDue, i.totalAmountDue, i.paymentStatus, i.generatedAt, i.invoicePdf) FROM Invoice i JOIN i.customer c WHERE c.meterNo = ?1")
    Page<InvoiceData> findByCustomerMeterNo(String meterNo, Pageable pageable);

    @Query("SELECT new com.bbc.app.dto.data.InvoiceData(i.invoiceId, c.user.name, c.meterNo, c.user.name, i.unitConsumption, i.billDuration, i.billDueDate, i.currentAmountDue, i.totalAmountDue, i.paymentStatus, i.generatedAt, i.invoicePdf) FROM Invoice i JOIN i.customer c WHERE c.user.name LIKE %?1%")
    Page<InvoiceData> findByCustomerUserNameContaining(String userName, Pageable pageable);

    @Query("SELECT new com.bbc.app.dto.data.InvoiceData(i.invoiceId, c.user.name, c.meterNo, c.user.name, i.unitConsumption, i.billDuration, i.billDueDate, i.currentAmountDue, i.totalAmountDue, i.paymentStatus, i.generatedAt, i.invoicePdf) FROM Invoice i JOIN i.customer c WHERE c.meterNo = ?1 AND i.billDuration LIKE %?2%")
    Page<InvoiceData> findByCustomerMeterNoAndBillDurationContaining(String meterNo, String billDuration, Pageable pageable);

    Long countByPaymentStatus(PaymentStatus paymentStatus);

    Collection<Object> findByPaymentStatus(PaymentStatus paymentStatus);

    Long countByCustomerMeterNoAndPaymentStatus(String meterNo, PaymentStatus paymentStatus);

    Optional<Invoice> findTopByCustomerMeterNoAndPaymentStatusOrderByGeneratedAtDesc(String meterNo, PaymentStatus paymentStatus);

    List<Invoice> findByCustomerMeterNoOrderByBillDurationAsc(String meterNo);
}
