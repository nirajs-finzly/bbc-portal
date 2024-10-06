package com.bbc.app.controller;

import com.bbc.app.dto.request.CreateInvoiceRequest;
import com.bbc.app.dto.response.InvoicesResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.SingleInvoiceResponse;
import com.bbc.app.dto.response.StatisticsResponse;
import com.bbc.app.service.InvoiceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/statistics/all-invoices-count")
    public ResponseEntity<StatisticsResponse> getAllInvoicesCount() {
        return invoiceService.getTotalInvoicesCount();
    }

    @GetMapping("/statistics/all-unpaid-invoices-count")
    public ResponseEntity<StatisticsResponse> getAllUnpaidInvoicesCount() {
        return invoiceService.getAllUnpaidInvoicesCount();
    }

    @GetMapping("/statistics/average-unit-consumption")
    public ResponseEntity<StatisticsResponse> getAverageUnitConsumptionOfAllCustomer() {
        return invoiceService.getUnitConsumptionDataForAllCustomers();
    }

    @GetMapping("/statistics/invoices-status-data")
    public ResponseEntity<StatisticsResponse> getInvoiceStatusData() {
        return invoiceService.getInvoiceStatusCount();
    }

    @GetMapping("/statistics/customer-invoices-count/{meterNo}")
    public ResponseEntity<StatisticsResponse> getAllCustomerInvoicesCount(@PathVariable String meterNo) {
        return invoiceService.getTotalInvoicesCountForCustomer(meterNo);
    }

    @GetMapping("/statistics/customer-unpaid-invoices-count/{meterNo}")
    public ResponseEntity<StatisticsResponse> getCustomerUnpaidInvoicesCount(@PathVariable String meterNo) {
        return invoiceService.getUnpaidInvoicesCountForCustomer(meterNo);
    }

    @GetMapping("/statistics/last-payment-amount/{meterNo}")
    public ResponseEntity<StatisticsResponse> getLastPaymentAmount(@PathVariable String meterNo) {
        return invoiceService.getLastPaymentAmountForCustomer(meterNo);
    }

    @GetMapping("/statistics/average-unit-consumption/{meterNo}")
    public ResponseEntity<StatisticsResponse> getAverageUnitConsumptionOfCustomer(@PathVariable String meterNo) {
        return invoiceService.getAverageUnitConsumptionByMeterNo(meterNo);
    }

    @GetMapping("")
    public ResponseEntity<InvoicesResponse> getAllInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return invoiceService.getAllInvoices(page, size);
    }

    @GetMapping("/{meterNo}")
    public ResponseEntity<InvoicesResponse> getInvoicesByCustomerMeterNo(
            @PathVariable @Pattern(regexp = "^(MTR)\\d{7}$", message = "Invalid meter number format") String meterNo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return invoiceService.getInvoicesByMeterNo(meterNo, page, size);
    }

    @GetMapping("/customer/{customerName}")
    public ResponseEntity<InvoicesResponse> getInvoicesByCustomerName(
            @PathVariable String customerName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return invoiceService.getInvoicesByCustomerName(customerName, page, size);
    }

    @GetMapping("/{meterNo}/{billDuration}")
    public ResponseEntity<InvoicesResponse> getInvoicesCustomerByBillDuration(
            @PathVariable @Pattern(regexp = "^(MTR)\\d{7}$", message = "Invalid meter number format") String meterNo,
            @PathVariable @NotBlank(message = "Bill duration is required") String billDuration,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return invoiceService.getInvoicesByCustomerBillDuration(meterNo, billDuration, page, size);
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> createInvoice(@Valid @RequestBody CreateInvoiceRequest request) {
        return invoiceService.createInvoice(request.getMeterNo(), request.getUnitsConsumed(), request.getBillDuration(), request.getBillDueDate());
    }

    @GetMapping("/{meterNo}/latest")
    public ResponseEntity<SingleInvoiceResponse> getLatestUnpaidInvoice(@PathVariable @Pattern(regexp = "^(MTR)\\d{7}$", message = "Invalid meter number format") String meterNo) {
        return invoiceService.getLatestInvoiceByMeterNo(meterNo);
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> bulkUploadInvoice(
            @RequestParam("file") MultipartFile dataFile,
            @RequestPart("billDuration") String billDuration,
            @RequestPart("billDueDate") String billDueDate
    ) throws IOException {
        return invoiceService.bulkUploadInvoice(dataFile, billDuration, LocalDate.parse(billDueDate));
    }

}
