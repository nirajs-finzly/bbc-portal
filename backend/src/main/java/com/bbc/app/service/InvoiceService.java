package com.bbc.app.service;

import com.bbc.app.dto.response.InvoicesResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.SingleInvoiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface InvoiceService {
    ResponseEntity<InvoicesResponse> getAllInvoices(int page, int size);

    ResponseEntity<InvoicesResponse> getInvoicesByMeterNo(String meterNo, int page, int size);

    ResponseEntity<MessageResponse> createInvoice(String meterNo, BigDecimal unitsConsumed, String billDuration, LocalDate billDueDate);

    ResponseEntity<SingleInvoiceResponse> getLatestInvoiceByMeterNo(String meterNo);

    ResponseEntity<MessageResponse> bulkUploadInvoice(MultipartFile dataFile,String billDuration,LocalDate billDueDate) throws IOException;

}
