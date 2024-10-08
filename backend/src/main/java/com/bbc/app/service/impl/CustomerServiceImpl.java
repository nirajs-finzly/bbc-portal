package com.bbc.app.service.impl;

import com.bbc.app.dto.data.CustomerData;
import com.bbc.app.dto.response.CustomersResponse;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.dto.response.StatisticsResponse;
import com.bbc.app.model.*;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.InvoiceRepository;
import com.bbc.app.repository.UserRepository;
import com.bbc.app.service.CustomerService;
import com.bbc.app.utils.Parsing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private Parsing parsing;

    @Override
    public ResponseEntity<StatisticsResponse> getTotalCustomersCount() {
        Long totalCustomersCount = customerRepository.count();
        return ResponseEntity.ok(new StatisticsResponse("Total Customers Count", totalCustomersCount, null, true));
    }

    @Override
    public ResponseEntity<CustomersResponse> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "user.name"));
        Page<Customer> customerPage = customerRepository.findAll(pageable);

        if (customerPage.isEmpty()) {
            return ResponseEntity.ok(new CustomersResponse("No customers found!", List.of(), false));
        }

        List<CustomerData> customerDataList = customerPage.getContent().stream()
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
    public ResponseEntity<CustomersResponse> getCustomersByMeterNo(String meterNo, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "user.name"));
        Page<Customer> customerPage = customerRepository.findByMeterNoContaining(meterNo, pageable);

        if (customerPage.isEmpty()) {
            return ResponseEntity.ok(new CustomersResponse("No customers found!", List.of(), false));
        }

        List<CustomerData> customerDataList = customerPage.getContent().stream()
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
    public ResponseEntity<MessageResponse> updateCustomer(String meterno, String name, String email, String phone, String address) {
        Optional<Customer> existingCustomer = customerRepository.findByMeterNo(meterno);

        if (existingCustomer.isEmpty()) {
            return ResponseEntity.ok(new MessageResponse("Customer not found!", false));
        }

        Customer customer = existingCustomer.get();

        customer.getUser().setName(name);
        customer.getUser().setEmail(email);
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

    public boolean hasUnpaidInvoices(String meterNo) {
        List<Invoice> unpaidInvoices = invoiceRepository.findByCustomerMeterNoAndPaymentStatus(meterNo, PaymentStatus.UNPAID);
        return !unpaidInvoices.isEmpty();
    }

    @Override
    public ResponseEntity<MessageResponse> bulkUploadCustomer(MultipartFile dataFile) throws IOException {

        if (!Objects.requireNonNull(dataFile.getOriginalFilename()).endsWith(".csv")) {

            return ResponseEntity.badRequest().body(new MessageResponse("Error! Invalid file format", false));
        }

        AtomicInteger invalidRecords = new AtomicInteger(0); // To keep track of invalid records

        List<Customer> customers = parsing.parseCSV(dataFile.getInputStream(), invalidRecords);

        int validRecords = customers.size();

        // Save each customer to the repository
        for (Customer customer : customers) {
            if (customer.isValid()) {
                // Save valid customer records
                userRepository.save(customer.getUser());
                customerRepository.save(customer);
            }
        }

        String message = String.format("Successfully uploaded: %d, Failed: %d", validRecords, invalidRecords.get());
        return ResponseEntity.ok(new MessageResponse(message, true));

    }


}
