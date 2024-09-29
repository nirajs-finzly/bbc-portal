package com.bbc.app.service;

import com.bbc.app.dto.response.InvoicesResponse;
import org.springframework.http.ResponseEntity;

public interface InvoiceService {
    ResponseEntity<InvoicesResponse> getInvoicesByMeterNo(String meterNo);
}
