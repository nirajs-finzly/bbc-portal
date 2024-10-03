package com.bbc.app.utils;

import com.bbc.app.exception.SomethingWrongException;
import com.bbc.app.model.Customer;
import com.bbc.app.model.Invoice;
import com.bbc.app.model.PaymentStatus;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.InvoiceRepository;
import com.bbc.app.service.PdfService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InvoiceParsing {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PdfService pdfService;

    private static final BigDecimal RATE_PER_KW = BigDecimal.valueOf(41.50);

    public List<Invoice> parseCSV(InputStream inputStream, AtomicInteger invalidRecords, String billDuration, LocalDate billDueDate) throws IOException {
        List<Invoice> invoiceList = new ArrayList<>();


        try (InputStreamReader reader = new InputStreamReader(inputStream);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {

                BigDecimal unitsConsumed = new BigDecimal(csvRecord.get("unit_consumption"));
                String meterNo = csvRecord.get("meter_no");


                // Validate unitConsumption ( unitConsumption validation)
                if (unitsConsumed.compareTo(BigDecimal.ZERO) <= 0) {
                    invalidRecords.incrementAndGet();
                    continue; // Skip this record and continue with the next one
                }

                // Validate billDueDate ( billDueDate validation)
                if (billDueDate == null || !billDueDate.toString().matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    invalidRecords.incrementAndGet();
                    continue; // Skip this record and continue with the next one
                }

                // Validate billDuration number
                if (billDuration == null || billDuration.isEmpty() || !billDuration.matches("^(January|February|March|April|May|June|July|August|September|October|November|December) \\d{4}$")) {
                    invalidRecords.incrementAndGet();
                    continue; // Skip this record and continue with the next one
                }

                // Validate meterNo ( meterNo validation)
                if (meterNo == null || meterNo.isEmpty() || !meterNo.matches("^MTR\\d{7}$")) {
                    invalidRecords.incrementAndGet();
                    continue; // Skip this record and continue with the next one
                }

                // Find the customer by meter number
                Optional<Customer> customer = customerRepository.findByMeterNo(meterNo);

                if (customer.isEmpty()) {
                    invalidRecords.incrementAndGet();
                    continue;
                }

                Optional<Invoice> existingInvoice = invoiceRepository.findByCustomerAndBillDuration(customer.get(), billDuration);
                if (existingInvoice.isPresent()) {
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
                } catch (IOException e) {
                    invalidRecords.incrementAndGet();
                    throw new SomethingWrongException("Failed to generate invoice PDF");
                }
                invoiceList.add(newInvoice);
            }
        }

        return invoiceList;
    }


}


