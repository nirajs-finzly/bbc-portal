package com.bbc.app.controller;

import com.bbc.app.dto.request.CreateCustomerRequest;
import com.bbc.app.dto.request.UpdateCustomerRequest;
import com.bbc.app.dto.response.CustomersResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.StatisticsResponse;
import com.bbc.app.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping("/statistics/total-customers-count")
    public ResponseEntity<StatisticsResponse> getTotalCustomersCount() {
        return customerService.getTotalCustomersCount();
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping("")
    public ResponseEntity<CustomersResponse> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return customerService.getAllCustomers(page, size);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PostMapping("")
    public ResponseEntity<MessageResponse> createCustomer(@RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request.getName(), request.getEmail(), request.getPhone(), request.getAddress());
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping("/{meterNo}")
    public ResponseEntity<CustomersResponse> getCustomerByMeterNo(@PathVariable String meterNo, @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "5") int size) {
        return customerService.getCustomersByMeterNo(meterNo, page, size);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PutMapping("/{meterNo}")
    public ResponseEntity<MessageResponse> updateCustomer(@PathVariable String meterNo, @RequestBody UpdateCustomerRequest request) {
        return customerService.updateCustomer(meterNo, request.getName(), request.getEmail(), request.getPhone(), request.getAddress());
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @DeleteMapping("/{meterNo}")
    public ResponseEntity<MessageResponse> deleteCustomer(@PathVariable String meterNo) {
        return customerService.deleteCustomer(meterNo);
    }

    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @GetMapping("/has-unpaid-invoices/{meterNo}")
    public ResponseEntity<Boolean> hasUnpaidInvoices(@PathVariable String meterNo) {
        boolean hasUnpaid = customerService.hasUnpaidInvoices(meterNo);
        return ResponseEntity.ok(hasUnpaid);
    }


    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> bulkUploadCustomer(@RequestParam("file") MultipartFile dataFile) throws IOException {
        return customerService.bulkUploadCustomer(dataFile);
    }

}
