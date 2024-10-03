package com.bbc.app.service.impl;

import com.bbc.app.dto.data.CustomerInvoiceData;
import com.bbc.app.dto.data.InvoiceData;
import com.bbc.app.dto.response.CustomerInvoicesResponse;
import com.bbc.app.dto.response.InvoicesResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.SingleInvoiceResponse;
import com.bbc.app.model.Customer;
import com.bbc.app.model.Invoice;
import com.bbc.app.model.PaymentStatus;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.InvoiceRepository;
import com.bbc.app.service.InvoiceService;
import com.bbc.app.service.PdfService;
import com.bbc.app.utils.InvoiceParsing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PdfService pdfService;

    @Autowired
    private InvoiceParsing invoiceParsing;

    private static final BigDecimal RATE_PER_KW = BigDecimal.valueOf(41.50);

    @Override
    public ResponseEntity<InvoicesResponse> getAllInvoices(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        System.out.println(page + " " + size);

        // Fetching all invoices with pagination
        Page<InvoiceData> invoicesPage = invoiceRepository.findAll(pageable)
                .map(this::convertToInvoiceData);

        invoicesPage.stream().forEach(invoiceData -> {
            System.out.println(invoiceData.getBillDuration());
        });

        Long totalInvoicesCount = invoiceRepository.count();

        if (invoicesPage.isEmpty()) {
            return ResponseEntity.ok(new InvoicesResponse("No invoices found!", List.of(), totalInvoicesCount, false));
        }

        InvoicesResponse response = new InvoicesResponse("Invoices retrieved successfully!", invoicesPage.getContent(), totalInvoicesCount, true);
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<CustomerInvoicesResponse> getInvoicesByMeterNo(String meterNo, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        Page<CustomerInvoiceData> invoicesPage = invoiceRepository.findByCustomerMeterNo(meterNo, pageable);
        Long totalInvoicesCount = invoiceRepository.countByCustomerMeterNo(meterNo);

        if (invoicesPage.isEmpty()) {
            return ResponseEntity.ok(new CustomerInvoicesResponse("No invoices found!", List.of(), 0L, false));
        }

        CustomerInvoicesResponse response = new CustomerInvoicesResponse("Invoices retrieved successfully!", invoicesPage.getContent(), totalInvoicesCount, true);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MessageResponse> createInvoice(String meterNo, BigDecimal unitsConsumed, String billDuration, LocalDate billDueDate) {

        Optional<Invoice> invoice = invoiceRepository.findByBillDuration(billDuration);
        if(invoice.isPresent()){
            return ResponseEntity.badRequest().body(new MessageResponse("Invoice already exists!", false));
        }

        // Calculate the amount for the current invoice based on the units consumed
        BigDecimal invoiceAmount = RATE_PER_KW.multiply(unitsConsumed);

        // Fetch unpaid invoices for the given customer
        List<Invoice> unpaidInvoices =
                invoiceRepository.findByCustomerMeterNoAndPaymentStatus(meterNo, PaymentStatus.UNPAID);

        // Calculate total unpaid amount by summing up unpaid invoices
        BigDecimal totalUnpaidAmount = unpaidInvoices.stream()
                .map(Invoice::getCurrentAmountDue)  // Only considering unpaid invoices
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Add the total unpaid amount to the current invoice amount
        BigDecimal newInvoiceAmount = invoiceAmount.add(totalUnpaidAmount);

        // Find the customer by meter number
        Optional<Customer> customer = customerRepository.findByMeterNo(meterNo);

        if (customer.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Customer not found!", false));
        }

        // Create and save the new invoice
        Invoice newInvoice = new Invoice();
        newInvoice.setCustomer(customer.get());
        newInvoice.setUnitConsumption(unitsConsumed);
        newInvoice.setBillDuration(billDuration);
        newInvoice.setBillDueDate(billDueDate);
        newInvoice.setCurrentAmountDue(invoiceAmount);
        newInvoice.setTotalAmountDue(newInvoiceAmount);
        newInvoice.setPaymentStatus(PaymentStatus.UNPAID);

        invoiceRepository.save(newInvoice);

        try {
            byte[] invoicePdf = pdfService.createInvoicePdf(newInvoice, unpaidInvoices);
            newInvoice.setInvoicePdf(invoicePdf);
            invoiceRepository.save(newInvoice);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error generating invoice PDF!", false));
        }

        return ResponseEntity.ok(new MessageResponse("New invoice created successfully!", true));
    }

    @Override
    public ResponseEntity<SingleInvoiceResponse> getLatestInvoiceByMeterNo(String meterNo) {
        Optional<Invoice> latestInvoice = invoiceRepository.findTopByCustomerMeterNoOrderByGeneratedAtDesc(meterNo);

        if (latestInvoice.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    new SingleInvoiceResponse(
                            "No unpaid invoice found!",
                            null,
                            false));
        }

        // Assuming there's a way to convert Invoice to InvoiceData
        InvoiceData invoiceData = convertToInvoiceData(latestInvoice.get());

        return ResponseEntity.ok(new SingleInvoiceResponse("Latest invoice retrieved successfully!", invoiceData, true));
    }

    private InvoiceData convertToInvoiceData(Invoice invoice) {
        return new InvoiceData(
                invoice.getInvoiceId(),
                invoice.getCustomer().getMeterNo(),
                invoice.getCustomer().getUser().getName(),
                invoice.getUnitConsumption(),
                invoice.getBillDuration(),
                invoice.getBillDueDate(),
                invoice.getCurrentAmountDue(),
                invoice.getTotalAmountDue(),
                invoice.getPaymentStatus(),
                invoice.getGeneratedAt(),
                invoice.getInvoicePdf()
        );
    }

    @Override
    public ResponseEntity<MessageResponse> bulkUploadInvoice(MultipartFile dataFile,String billDuration,LocalDate billDueDate) throws IOException {

        if (!Objects.requireNonNull(dataFile.getOriginalFilename()).endsWith(".csv")) {

            return ResponseEntity.badRequest().body(new MessageResponse("Error! Invalid file format",false));
        }

        AtomicInteger invalidRecords = new AtomicInteger(0); // To keep track of invalid records

        List<Invoice> invoices = invoiceParsing.parseCSV(dataFile.getInputStream(),invalidRecords,billDuration,billDueDate);
        // Save each customer to the repository
        for (Invoice invoice : invoices) {
            if (invoice.isValid()) {
                // Save valid customer records
                invoiceRepository.save(invoice);
            }
        }

        int validRecords = invoices.size();

        String message = String.format("Data upload complete. Successfully uploaded: %d, Failed: %d", validRecords, invalidRecords.get());
        return ResponseEntity.ok(new MessageResponse(message, true));

    }
}