package com.bbc.app.service;

import com.bbc.app.dto.request.CreateCustomerRequest;
import com.bbc.app.dto.request.UpdateCustomerRequest;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.model.Customer;
import com.bbc.app.model.PaymentTransaction;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface CustomerService {

    // Retrieve all customers
    List<Customer> getAllCustomers();

    // Retrieve a customer by meter number
    Customer getCustomerByMeterno(String meterno);

    // Create a new customer
    Customer createCustomer(List<CreateCustomerRequest> customer);

    // Update an existing customer
    Customer updateCustomer(String meterno, UpdateCustomerRequest customer);

    // Delete a customer
    String deleteCustomer(String meterno);

    //get transactions by customerId
    List<PaymentTransaction> getTransactionsForCustomer(String customerId);

    // Bulk upload customers
    ResponseEntity<MessageResponse> bulkUploadCustomer(MultipartFile dataFile) throws IOException;
}
