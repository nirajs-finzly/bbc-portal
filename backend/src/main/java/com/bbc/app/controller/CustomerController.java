package com.bbc.app.controller;

import com.bbc.app.dto.request.CreateCustomerRequest;
import com.bbc.app.dto.request.UpdateCustomerRequest;
import com.bbc.app.dto.response.CustomersResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.SingleCustomerResponse;
import com.bbc.app.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("")
    public ResponseEntity<CustomersResponse> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return customerService.getAllCustomers(page, size);
    }

    @PostMapping("")
    public ResponseEntity<MessageResponse> createCustomer(@RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request.getName(), request.getEmail(), request.getPhone(), request.getAddress());
    }

    @GetMapping("/{meterNo}")
    public ResponseEntity<SingleCustomerResponse> getCustomerByMeterno(@PathVariable String meterNo) {
        return customerService.getCustomerByMeterno(meterNo);
    }

    @PutMapping("/{meterNo}")
    public ResponseEntity<MessageResponse> updateCustomer(@PathVariable String meterNo ,@RequestBody UpdateCustomerRequest request) {
        return customerService.updateCustomer(meterNo, request.getName(), request.getPhone(), request.getAddress());
    }

    @DeleteMapping("/{meterNo}")
    public ResponseEntity<MessageResponse> deleteCustomer(@PathVariable String meterNo) {
        return customerService.deleteCustomer(meterNo);
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> bulkUploadCustomer(@RequestParam("file") MultipartFile dataFile) throws IOException {
        return customerService.bulkUploadCustomer(dataFile);
    }

}
