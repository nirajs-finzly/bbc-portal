package com.bbc.app.service;

import com.bbc.app.dto.response.InvoicesResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.SingleInvoiceResponse;
import com.bbc.app.dto.response.StatisticsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface InvoiceService {
    ResponseEntity<StatisticsResponse> getTotalInvoicesCount();

    ResponseEntity<StatisticsResponse> getAllUnpaidInvoicesCount();

    ResponseEntity<StatisticsResponse> getTotalInvoicesCountForCustomer(String meterNo);

    ResponseEntity<StatisticsResponse> getUnpaidInvoicesCountForCustomer(String meterNo);

    ResponseEntity<StatisticsResponse> getLastPaymentAmountForCustomer(String meterNo);

    ResponseEntity<StatisticsResponse> getAverageUnitConsumptionByMeterNo(String meterNo);

    ResponseEntity<StatisticsResponse> getUnitConsumptionDataForAllCustomers();

    ResponseEntity<StatisticsResponse> getInvoiceStatusCount();

    ResponseEntity<InvoicesResponse> getAllInvoices(int page, int size);

    ResponseEntity<InvoicesResponse> getInvoicesByMeterNo(String meterNo, int page, int size);

    ResponseEntity<InvoicesResponse> getInvoicesByCustomerName(String customerName, int page, int size);

    ResponseEntity<InvoicesResponse> getInvoicesByCustomerBillDuration(String meterNo, String billDuration, int page, int size);

    ResponseEntity<MessageResponse> createInvoice(String meterNo, BigDecimal unitsConsumed, String billDuration, LocalDate billDueDate);

    ResponseEntity<SingleInvoiceResponse> getLatestInvoiceByMeterNo(String meterNo);

    ResponseEntity<MessageResponse> bulkUploadInvoice(MultipartFile dataFile,String billDuration,LocalDate billDueDate) throws IOException;

}
