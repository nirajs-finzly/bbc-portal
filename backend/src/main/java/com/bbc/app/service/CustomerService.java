package com.bbc.app.service;

import com.bbc.app.dto.response.CustomerResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {

    // Retrieve all customers
    List<Customer> getAllCustomers();

    // Retrieve a customer by meter number
    ResponseEntity<CustomerResponse> getCustomerByMeterno(String meterno);

    // Create a new customer
    ResponseEntity<MessageResponse> createCustomer(String name, String email, String phone, String address);

    // Update an existing customer
    ResponseEntity<MessageResponse> updateCustomer(String meterno, String name, String email, String phone, String address);

    // Delete a customer
    ResponseEntity<MessageResponse> deleteCustomer(String meterno);

    // Bulk upload customers
//    ResponseEntity<MessageResponse> bulkUploadCustomer(MultipartFile dataFile) throws IOException;
}
