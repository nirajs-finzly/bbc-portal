package com.bbc.app.controller;

import com.bbc.app.dto.request.CreateCustomerRequest;
import com.bbc.app.dto.request.UpdateCustomerRequest;
import com.bbc.app.dto.response.ErrorResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.model.Customer;
import com.bbc.app.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    public Object createCustomer(@RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request.getName(), request.getEmail(), request.getPhone(), request.getAddress());
    }

    @GetMapping("/get-all")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/{meterNo}")
    public ResponseEntity<Object> getCustomerByMeterno(@RequestParam String meterNo) {
        try {
            return ResponseEntity.ok(customerService.getCustomerByMeterno(meterNo));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<MessageResponse> updateCustomer(@RequestHeader String meterNo, @RequestBody UpdateCustomerRequest request) {
        return customerService.updateCustomer(meterNo, request.getName(), request.getEmail(), request.getPhone(), request.getAddress());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<MessageResponse> deleteCustomer(@RequestHeader String meterNo) {
        return customerService.deleteCustomer(meterNo);
    }

//    @PostMapping("/upload")
//    public ResponseEntity<MessageResponse> bulkUploadCustomer(@RequestParam("file") MultipartFile dataFile) throws IOException {
//
//        return customerService.bulkUploadCustomer(dataFile);
//
//    }

}
