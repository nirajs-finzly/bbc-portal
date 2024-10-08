package com.bbc.app.service.impl;

import com.bbc.app.dto.data.InvoiceData;
import com.bbc.app.dto.data.StatisticsData;
import com.bbc.app.dto.response.InvoicesResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.SingleInvoiceResponse;
import com.bbc.app.dto.response.StatisticsResponse;
import com.bbc.app.model.Customer;
import com.bbc.app.model.Invoice;
import com.bbc.app.model.PaymentStatus;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.InvoiceRepository;
import com.bbc.app.service.EmailService;
import com.bbc.app.service.InvoiceService;
import com.bbc.app.service.PdfService;
import com.bbc.app.utils.InvoiceParsing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
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
    private EmailService emailService;

    @Autowired
    private InvoiceParsing invoiceParsing;

    private static final BigDecimal RATE_PER_KW = BigDecimal.valueOf(41.50);

    @Override
    public ResponseEntity<StatisticsResponse> getTotalInvoicesCount() {
        Long invoicesCount = invoiceRepository.count();
        return ResponseEntity.ok(new StatisticsResponse("Total Invoices Count", invoicesCount, null, true)); // Method 1: Returns the total number of invoices
    }

    @Override
    public ResponseEntity<StatisticsResponse> getAllUnpaidInvoicesCount() {
        Long unpaidInvoicesCount = invoiceRepository.countByPaymentStatus(PaymentStatus.UNPAID); // Method 3: Returns the total number of unpaid invoices
        return ResponseEntity.ok(new StatisticsResponse("Total Unpaid Invoices Count", unpaidInvoicesCount, null, true));
    }

    @Override
    public ResponseEntity<StatisticsResponse> getTotalInvoicesCountForCustomer(String meterNo) {
        Long totalInvoicesCount = invoiceRepository.countByCustomerMeterNo(meterNo);
        return ResponseEntity.ok(new StatisticsResponse("Total Invoices Count", totalInvoicesCount, null, true));
    }

    @Override
    public ResponseEntity<StatisticsResponse> getUnpaidInvoicesCountForCustomer(String meterNo) {
        Long unpaidInvoicesCount = invoiceRepository.countByCustomerMeterNoAndPaymentStatus(meterNo, PaymentStatus.UNPAID);
        return ResponseEntity.ok(new StatisticsResponse("Total Unpaid Invoices Count", unpaidInvoicesCount, null, true));
    }

    @Override
    public ResponseEntity<StatisticsResponse> getLastPaymentAmountForCustomer(String meterNo) {
        Optional<Invoice> latestPaidInvoice = invoiceRepository.findTopByCustomerMeterNoAndPaymentStatusOrderByGeneratedAtDesc(meterNo, PaymentStatus.PAID);

        if (latestPaidInvoice.isEmpty()) {
            return ResponseEntity.ok(new StatisticsResponse("No paid invoices found for the customer", 0L, null, true));
        }

        BigDecimal totalAmount = latestPaidInvoice.map(Invoice::getTotalAmountDue).orElse(BigDecimal.ZERO);

        StatisticsData data = new StatisticsData();
        data.setAmount(totalAmount);

        return ResponseEntity.ok(new StatisticsResponse("Last Payment Amount", 1L, data, true));
    }

    @Override
    public ResponseEntity<StatisticsResponse> getAverageUnitConsumptionByMeterNo(String meterNo) {
        // Fetch all invoices for the customer by meter number, sorted by bill duration
        List<Invoice> invoices = invoiceRepository.findByCustomerMeterNoOrderByBillDurationAsc(meterNo);

        // Check if no invoices are found
        if (invoices.isEmpty()) {
            return ResponseEntity.ok(new StatisticsResponse("No invoices found for the specified meter number!", 0L, null, false));
        }

        // Calculate the total unit consumption for all invoices
        BigDecimal totalUnitConsumption = invoices.stream()
                .map(Invoice::getUnitConsumption)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate the average unit consumption
        BigDecimal averageUnitConsumption = totalUnitConsumption.divide(BigDecimal.valueOf(invoices.size()), 2, RoundingMode.HALF_UP);

        // Return the response with the calculated average
        return ResponseEntity.ok(new StatisticsResponse("Average unit consumption calculated successfully.", 0L, new StatisticsData(null, averageUnitConsumption, null, null), true));
    }

    @Override
    public ResponseEntity<StatisticsResponse> getUnitConsumptionDataForAllCustomers() {
        // Fetch all invoices sorted by customer and bill duration
        List<Invoice> invoices = invoiceRepository.findAll(Sort.by(Sort.Direction.ASC, "customer.meterNo", "billDuration"));

        if (invoices.isEmpty()) {
            return ResponseEntity.ok(new StatisticsResponse(
                    "No invoices found for any customer!", 0L, null,
                    false));
        }

        // Create a map to store the unit consumption data by year
        Map<String, BigDecimal> consumptionDataByYear = new LinkedHashMap<>();

        // Create a map to track the number of customers with invoices for each year
        Map<String, Integer> customerCountsByYear = new LinkedHashMap<>();

        // Calculate total unit consumption for each year across all customers
        invoices.forEach(invoice -> {
            String billDuration = invoice.getBillDuration();
            if (billDuration != null && !billDuration.trim().isEmpty()) {
                try {
                    // Extract the year from billDuration (assuming it's in the format 'MMMM yyyy', e.g., 'April 2025')
                    String[] parts = billDuration.split(" ");
                    if (parts.length == 2) {
                        String year = parts[1]; // Get the year part

                        BigDecimal unitConsumption = invoice.getUnitConsumption();
                        if (unitConsumption != null) {
                            // Accumulate unit consumption for each year
                            consumptionDataByYear.merge(year, unitConsumption, BigDecimal::add);

                            // Track how many customers have invoices for this year
                            customerCountsByYear.merge(year, 1, Integer::sum);
                        }
                    }
                } catch (Exception e) {
                    // Log any errors during parsing of billDuration or unitConsumption
                    System.err.println("Error processing invoice: " + e.getMessage());
                }
            }
        });

        // Calculate average consumption per year by dividing the total by the number of customers with invoices
        consumptionDataByYear.replaceAll((year, totalConsumption) ->
                totalConsumption.divide(BigDecimal.valueOf(customerCountsByYear.get(year)), 2, RoundingMode.HALF_UP)
        );

        return ResponseEntity.ok(new StatisticsResponse(
                "Year-wise Unit Consumption Data for All Customers",
                (long) consumptionDataByYear.size(),
                new StatisticsData(null, null, consumptionDataByYear, null),
                true
        ));
    }


    @Override
    public ResponseEntity<StatisticsResponse> getInvoiceStatusCount() {
        // Count the number of paid invoices
        Long paidInvoicesCount = invoiceRepository.countByPaymentStatus(PaymentStatus.PAID);

        // Count the number of unpaid invoices
        Long unpaidInvoicesCount = invoiceRepository.countByPaymentStatus(PaymentStatus.UNPAID);

        // Create a map to hold the pie chart data (paid/unpaid counts)
        Map<String, Long> invoiceStatusData = new LinkedHashMap<>();
        invoiceStatusData.put("Paid", paidInvoicesCount);
        invoiceStatusData.put("Unpaid", unpaidInvoicesCount);

        // Return the response with the pie chart data
        return ResponseEntity.ok(new StatisticsResponse("Invoice Status Data", 0L, new StatisticsData(null, null, null, invoiceStatusData), true));
    }


    @Override
    public ResponseEntity<InvoicesResponse> getAllInvoices(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "generatedAt"));

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
    public ResponseEntity<InvoicesResponse> getInvoicesByMeterNo(String meterNo, int page, int size) {
        // Add sorting by generatedAt in descending order
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "generatedAt"));

        Page<InvoiceData> invoicesPage = invoiceRepository.findByCustomerMeterNo(meterNo, pageable);
        Long totalInvoicesCount = invoiceRepository.countByCustomerMeterNo(meterNo);

        if (invoicesPage.isEmpty()) {
            return ResponseEntity.ok(new InvoicesResponse("No invoices found!", List.of(), 0L, false));
        }

        InvoicesResponse response = new InvoicesResponse(
                "Invoices retrieved successfully!",
                invoicesPage.getContent(),
                totalInvoicesCount,
                true
        );

        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<InvoicesResponse> getInvoicesByCustomerName(String customerName, int page, int size) {
        // Create a pageable object with sorting by "generatedAt" in descending order
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "generatedAt"));

        // Fetch paginated invoices by customer name
        Page<InvoiceData> invoicesPage = invoiceRepository.findByCustomerUserNameContaining(customerName, pageable);

        // If no invoices are found, return a response with an empty list
        if (!invoicesPage.hasContent()) {
            return ResponseEntity.ok(new InvoicesResponse("No invoices found for customer: " + customerName, List.of(), 0L, false));
        }

        // Create the response object with retrieved invoices
        InvoicesResponse response = new InvoicesResponse(
                "Invoices retrieved successfully!",
                invoicesPage.getContent(),
                invoicesPage.getTotalElements(),
                true
        );

        // Return the response entity with the InvoicesResponse object
        return ResponseEntity.ok(response);
    }


    @Override
    public ResponseEntity<InvoicesResponse> getInvoicesByCustomerBillDuration(String meterNo, String billDuration, int page, int size) {
        try {
            // Create pageable object for pagination
            PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "generatedAt"));

            // Fetch invoices that match the meter number and partial bill duration with pagination
            Page<InvoiceData> invoicesPage = invoiceRepository.findByCustomerMeterNoAndBillDurationContaining(meterNo, billDuration, pageable);

            // Check if no invoices match the criteria
            if (invoicesPage.isEmpty()) {
                return ResponseEntity.ok(new InvoicesResponse("No invoices found for the specified meter number and bill duration!", List.of(), 0L, false));
            }

            // Create a response with the invoices and pagination details
            InvoicesResponse response = new InvoicesResponse(
                    "Invoices retrieved successfully!",
                    invoicesPage.getContent(),
                    invoicesPage.getTotalElements(),
                    true
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to retrieve invoices", e);
        }
    }


    @Override
    public ResponseEntity<MessageResponse> createInvoice(String meterNo, BigDecimal unitsConsumed, String billDuration, LocalDate billDueDate) {
        Optional<Customer> customer = customerRepository.findByMeterNo(meterNo);
        if (customer.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Customer not found!", false));
        }

        Optional<Invoice> invoice = invoiceRepository.findByCustomerAndBillDuration(customer.get(), billDuration);
        if (invoice.isPresent()) {
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

        if (latestInvoice.isEmpty() || latestInvoice.get().getPaymentStatus() == PaymentStatus.PAID) {
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
                invoice.getCustomer().getUser().getName(),
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
    public ResponseEntity<MessageResponse> bulkUploadInvoice(MultipartFile dataFile, String billDuration, LocalDate billDueDate) throws IOException {

        if (!Objects.requireNonNull(dataFile.getOriginalFilename()).endsWith(".csv")) {

            return ResponseEntity.badRequest().body(new MessageResponse("Error! Invalid file format", false));
        }

        AtomicInteger invalidRecords = new AtomicInteger(0); // To keep track of invalid records

        List<Invoice> invoices = invoiceParsing.parseCSV(dataFile.getInputStream(), invalidRecords, billDuration, billDueDate);
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