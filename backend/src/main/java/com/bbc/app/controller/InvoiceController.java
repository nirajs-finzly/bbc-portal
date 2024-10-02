package com.bbc.app.controller;

import com.bbc.app.dto.request.CreateInvoiceRequest;
import com.bbc.app.dto.response.InvoicesResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.SingleInvoiceResponse;
import com.bbc.app.service.InvoiceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/{meterNo}")
    public ResponseEntity<InvoicesResponse> getInvoicesByCustomerMeterNo(
            @PathVariable @Pattern(regexp = "^(MTR)\\d{7}$", message = "Invalid meter number format") String meterNo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return invoiceService.getInvoicesByMeterNo(meterNo, page, size);
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
    public ResponseEntity<MessageResponse> bulkUploadInvoice(@RequestParam("file") MultipartFile dataFile) throws IOException {
        return invoiceService.bulkUploadInvoice(dataFile);
    }
}
