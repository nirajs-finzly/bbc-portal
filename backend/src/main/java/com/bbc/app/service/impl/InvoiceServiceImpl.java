package com.bbc.app.service.impl;

import com.bbc.app.dto.data.InvoiceData;
import com.bbc.app.dto.response.InvoicesResponse;
import com.bbc.app.repository.InvoiceRepository;
import com.bbc.app.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public ResponseEntity<InvoicesResponse> getInvoicesByMeterNo(String meterNo) {
        List<InvoiceData> invoices = invoiceRepository.findByCustomerMeterNo(meterNo);

        if (invoices == null || invoices.isEmpty()) {
            return ResponseEntity.ok(new InvoicesResponse("No invoices found for the customer", List.of(), false));
        }

        InvoicesResponse response = new InvoicesResponse("Invoices retrieved successfully", invoices, true);
        return ResponseEntity.ok(response);
    }
}