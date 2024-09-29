package com.bbc.app.controller;

import com.bbc.app.dto.response.InvoicesResponse;
import com.bbc.app.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/customer/{meterNo}")
    public ResponseEntity<InvoicesResponse> getInvoicesByCustomerId(@PathVariable String meterNo) {

        return invoiceService.getInvoicesByMeterNo(meterNo);
    }
}
