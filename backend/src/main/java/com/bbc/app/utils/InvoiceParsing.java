package com.bbc.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.model.*;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.InvoiceRepository;
import com.bbc.app.service.PdfService;
import com.bbc.app.service.impl.InvoiceServiceImpl;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class InvoiceParsing {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PdfService pdfService;

    private static final BigDecimal RATE_PER_KW = BigDecimal.valueOf(41.50);

    public List<Invoice> parseCSV(InputStream inputStream, AtomicInteger invalidRecords) throws IOException {
        List<Invoice> invoiceList = new ArrayList<>();


        try (InputStreamReader reader = new InputStreamReader(inputStream);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                LocalDate billDueDate = LocalDate.parse(csvRecord.get("bill_due_date"),formatter);
                String billDuration = csvRecord.get("bill_duration");
                BigDecimal unitsConsumed = new BigDecimal(csvRecord.get("unit_consumption"));
                String meterNo = csvRecord.get("meter_no");


                // Validate unitConsumption ( unitConsumption validation)
                if (unitsConsumed == null || unitsConsumed.compareTo(BigDecimal.ZERO) <= 0) {
                    invalidRecords.incrementAndGet();
                    continue; // Skip this record and continue with the next one
                }

                // Validate billDueDate ( billDueDate validation)
                if (billDueDate == null || !billDueDate.toString().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    invalidRecords.incrementAndGet();
                    continue; // Skip this record and continue with the next one
                }

                // Validate billDuration number
                if (billDuration == null ||billDuration.isEmpty()|| !billDuration.matches("^(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)-\\d{2}$")) {
                    invalidRecords.incrementAndGet();
                    continue; // Skip this record and continue with the next one
                }

                // Validate meterNo ( meterNo validation)
                if (meterNo == null ||meterNo.isEmpty()|| !meterNo.matches("^MTR\\d{7}$")){
                    invalidRecords.incrementAndGet();
                    continue; // Skip this record and continue with the next one
                }

                Optional<Invoice> invoice = invoiceRepository.findByBillDuration(billDuration);
                if(invoice.isPresent()){
//                    return ResponseEntity.badRequest().body(new MessageResponse("Invoice already exists!", false));
                    invalidRecords.incrementAndGet();
                    continue;
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
//                    return ResponseEntity.badRequest().body(new MessageResponse("Customer not found!", false));
                    invalidRecords.incrementAndGet();
                    continue;
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

                try {
                    byte[] invoicePdf = pdfService.createInvoicePdf(newInvoice, unpaidInvoices);
                    newInvoice.setInvoicePdf(invoicePdf);
                    invoiceRepository.save(newInvoice);
                } catch (IOException e) {
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                            .body(new MessageResponse("Error generating invoice PDF!", false));
                    invalidRecords.incrementAndGet();
                }
            }
        }

        return invoiceList;
    }




}


