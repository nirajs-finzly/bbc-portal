package com.bbc.app.service;

import com.bbc.app.dto.response.CustomersResponse;
import com.bbc.app.dto.response.SingleCustomerResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface CustomerService {

    // Retrieve all customers
    ResponseEntity<CustomersResponse> getAllCustomers();

    // Retrieve a customer by meter number
    ResponseEntity<SingleCustomerResponse> getCustomerByMeterno(String meterno);

    // Create a new customer
    ResponseEntity<MessageResponse> createCustomer(String name, String email, String phone, String address);

    // Update an existing customer
    ResponseEntity<MessageResponse> updateCustomer(String meterno, String name, String phone, String address);

    // Delete a customer
    ResponseEntity<MessageResponse> deleteCustomer(String meterno);

    // Bulk upload customers
    ResponseEntity<MessageResponse> bulkUploadCustomer(MultipartFile dataFile) throws IOException;
}
