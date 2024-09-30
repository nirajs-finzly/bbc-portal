package com.bbc.app.service.impl;

import com.bbc.app.dto.response.CustomerResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.model.Customer;
import com.bbc.app.model.User;
import com.bbc.app.model.UserRole;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.PaymentTransactionRepsitory;
import com.bbc.app.repository.UserRepository;
import com.bbc.app.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentTransactionRepsitory paymentTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public ResponseEntity<CustomerResponse> getCustomerByMeterno(String meterNo) {
        Optional<Customer> customer = customerRepository.findByMeterNo(meterNo);

        return customer.map(value ->
                        ResponseEntity.ok(new CustomerResponse(
                                "Customer found", value, true)))
                .orElseGet(() -> ResponseEntity.ok(
                        new CustomerResponse(
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

            return ResponseEntity.ok(new MessageResponse("Customer created successfully."));
        }

        return ResponseEntity.ok(new MessageResponse("Customer already exists!"));
    }

    @Override
    public ResponseEntity<MessageResponse> updateCustomer(String meterno, String name, String email, String phone, String address) {
        Optional<Customer> existingCustomer = customerRepository.findByMeterNo(meterno);

        if (existingCustomer.isEmpty()) {
            return ResponseEntity.ok(new MessageResponse("Customer not found!"));
        }

        Customer customer = existingCustomer.get();

        customer.setAddress(address);
        customer.getUser().setName(name);
        customer.getUser().setEmail(email);
        customer.getUser().setPhone(phone);
        // Update other fields as necessary
        return ResponseEntity.ok(new MessageResponse("Customer updated successfully."));
    }

    @Override
    public ResponseEntity<MessageResponse> deleteCustomer(String meterno) {
        Optional<Customer> customer = customerRepository.findByMeterNo(meterno);
        if (customer.isEmpty()) {
            return ResponseEntity.ok(new MessageResponse("Customer not found!"));
        }

        customerRepository.deleteById(customer.get().getCustomerId());
        return ResponseEntity.ok(new MessageResponse("Customer deleted successfully."));
    }

//    @Override
//    public ResponseEntity<MessageResponse> bulkUploadCustomer(MultipartFile dataFile) throws IOException {
//
//        Map<String, Object> response = new HashMap<String, Object>();
//        if (!Objects.requireNonNull(dataFile.getOriginalFilename()).endsWith(".csv")) {
//
//            return ResponseEntity.badRequest().body(new MessageResponse("Error! Invalid file format"));
//        }
//        List<Customer> customers = CustomerParsing.parseCSV(dataFile.getInputStream());
//        return ResponseEntity.ok(new MessageResponse("Data uploaded successfully."));
//
//    }


}
