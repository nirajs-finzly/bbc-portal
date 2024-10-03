package com.bbc.app.service.impl;

import com.bbc.app.dto.data.CustomerData;
import com.bbc.app.dto.response.CustomersResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.SingleCustomerResponse;
import com.bbc.app.model.Customer;
import com.bbc.app.model.User;
import com.bbc.app.model.UserRole;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.PaymentTransactionRepsitory;
import com.bbc.app.repository.UserRepository;
import com.bbc.app.service.CustomerService;
import com.bbc.app.utils.Parsing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentTransactionRepsitory paymentTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Parsing parsing;

    @Override
    public ResponseEntity<CustomersResponse> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();

        if (customers.isEmpty()) {
            return ResponseEntity.ok(new CustomersResponse("No customers found!", List.of(), false));
        }

        List<CustomerData> customerDataList = customers.stream()
                .map(customer -> new CustomerData(
                        customer.getUser().getName(),
                        customer.getUser().getEmail(),
                        customer.getUser().getPhone(),
                        customer.getAddress(),
                        customer.getMeterNo()
                ))
                .toList();

        return ResponseEntity.ok(new CustomersResponse("Customers data found!", customerDataList, true));
    }

    @Override
    public ResponseEntity<SingleCustomerResponse> getCustomerByMeterno(String meterNo) {
        Optional<Customer> customer = customerRepository.findByMeterNo(meterNo);

        return customer.map(value ->
                        ResponseEntity.ok(new SingleCustomerResponse(
                                "Customer found", value, true)))
                .orElseGet(() -> ResponseEntity.ok(
                        new SingleCustomerResponse(
                                "Customer not found", null, false)));

    }

    @Override
    public ResponseEntity<MessageResponse> createCustomer(String name, String email, String phone, String address) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            User newUser = new User();
            newUser.setRole(UserRole.valueOf(UserRole.CUSTOMER.name()));
            newUser.setPhone(phone);
            newUser.setName(name);
            newUser.setEmail(email);
            userRepository.save(newUser);
            Customer customer = new Customer();
            customer.setUser(newUser);
            customer.setAddress(address);
            customerRepository.save(customer);

            return ResponseEntity.ok(new MessageResponse("Customer created successfully.", true));
        }

        return ResponseEntity.ok(new MessageResponse("Customer already exists!", false));
    }

    @Override
    public ResponseEntity<MessageResponse> updateCustomer(String meterno, String name, String phone, String address) {
        Optional<Customer> existingCustomer = customerRepository.findByMeterNo(meterno);

        if (existingCustomer.isEmpty()) {
            return ResponseEntity.ok(new MessageResponse("Customer not found!", false));
        }

        Customer customer = existingCustomer.get();

        customer.getUser().setName(name);
        customer.getUser().setPhone(phone);
        customer.setAddress(address);
        customerRepository.save(customer);

        return ResponseEntity.ok(new MessageResponse("Customer updated successfully.", true));
    }

    @Override
    public ResponseEntity<MessageResponse> deleteCustomer(String meterNo) {
        Optional<Customer> customer = customerRepository.findByMeterNo(meterNo);
        if (customer.isEmpty()) {
            return ResponseEntity.ok(new MessageResponse("Customer not found!", false));
        }

        customerRepository.deleteById(meterNo);
        userRepository.deleteById(customer.get().getUser().getId());
        return ResponseEntity.ok(new MessageResponse("Customer deleted successfully.", true));
    }

    @Override
    public ResponseEntity<MessageResponse> bulkUploadCustomer(MultipartFile dataFile) throws IOException {

        if (!Objects.requireNonNull(dataFile.getOriginalFilename()).endsWith(".csv")) {

            return ResponseEntity.badRequest().body(new MessageResponse("Error! Invalid file format",false));
        }

        AtomicInteger invalidRecords = new AtomicInteger(0); // To keep track of invalid records

        List<Customer> customers = parsing.parseCSV(dataFile.getInputStream(),invalidRecords);

        int validRecords = customers.size();

        // Save each customer to the repository
        for (Customer customer : customers) {
            if (customer.isValid()) {
                // Save valid customer records
                userRepository.save(customer.getUser());
                customerRepository.save(customer);
            }
        }

        String message = String.format("Data upload complete. Successfully uploaded: %d, Failed: %d", validRecords, invalidRecords.get());
        return ResponseEntity.ok(new MessageResponse(message, true));

    }


}
