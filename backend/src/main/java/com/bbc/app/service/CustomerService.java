package com.bbc.app.service;

import com.bbc.app.dto.response.CustomersResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.StatisticsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface CustomerService {

    ResponseEntity<StatisticsResponse> getTotalCustomersCount();

    // Retrieve all customers
    ResponseEntity<CustomersResponse> getAllCustomers(int page, int size);

    ResponseEntity<CustomersResponse> getCustomersByMeterNo(String meterNo, int page, int size);

    // Create a new customer
    ResponseEntity<MessageResponse> createCustomer(String name, String email, String phone, String address);

    // Update an existing customer
    ResponseEntity<MessageResponse> updateCustomer(String meterno, String name, String email, String phone, String address);

    // Delete a customer
    ResponseEntity<MessageResponse> deleteCustomer(String meterno);

    //check if user has any unpaid invoices pending
    public boolean hasUnpaidInvoices(String meterNo);

    // Bulk upload customers
    ResponseEntity<MessageResponse> bulkUploadCustomer(MultipartFile dataFile) throws IOException;
}
