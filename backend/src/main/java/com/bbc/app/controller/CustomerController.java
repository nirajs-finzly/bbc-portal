package com.bbc.app.controller;

import com.bbc.app.dto.request.CreateCustomerRequest;
import com.bbc.app.dto.request.UpdateCustomerRequest;
import com.bbc.app.dto.response.ErrorResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.model.Customer;
import com.bbc.app.service.CustomerService;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    public Object createCustomer(@RequestBody List<CreateCustomerRequest> reqList)
    {
        Map<String,Object> response = new HashMap<String, Object>();
        try {
            customerService.createCustomer(reqList);
            response.put("statusCode", 200);
            response.put("message","Success");
        }catch (Exception e)
        {e.printStackTrace();
            ErrorResponse errorResponse = new ErrorResponse(e.getLocalizedMessage());
            return errorResponse;

        }
        return  response;
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
    public ResponseEntity<Object> updateCustomer(@RequestHeader String meterNo, @RequestBody UpdateCustomerRequest updateRequest) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(meterNo, updateRequest);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteCustomer(@RequestHeader String meterNo) {
        try {
            customerService.deleteCustomer(meterNo);
            return ResponseEntity.ok(Map.of("message", "Customer deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> bulkUploadCustomer(@RequestParam("file") MultipartFile dataFile) throws IOException {

        return customerService.bulkUploadCustomer(dataFile);

    }

}
