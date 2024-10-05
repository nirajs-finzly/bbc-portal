package com.bbc.app.controller;

import com.bbc.app.dto.request.CreateInvoiceRequest;
import com.bbc.app.dto.response.CustomerInvoicesResponse;
import com.bbc.app.dto.response.InvoicesResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.SingleInvoiceResponse;
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

    @GetMapping("")
    public ResponseEntity<InvoicesResponse> getAllInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        System.out.println(page + " " + size);
        return invoiceService.getAllInvoices(page, size);
    }

    @GetMapping("/{meterNo}")
    public ResponseEntity<CustomerInvoicesResponse> getInvoicesByCustomerMeterNo(
            @PathVariable @Pattern(regexp = "^(MTR)\\d{7}$", message = "Invalid meter number format") String meterNo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return invoiceService.getInvoicesByMeterNo(meterNo, page, size);
    }

    @GetMapping("/customer/{customerName}")
    public ResponseEntity<CustomerInvoicesResponse> getInvoicesByCustomerName(
            @PathVariable String customerName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return invoiceService.getInvoicesByCustomerName(customerName, page, size);
    }

    @GetMapping("/{meterNo}/{billDuration}")
    public ResponseEntity<CustomerInvoicesResponse> getInvoicesCustomerByBillDuration(
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
