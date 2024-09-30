package com.bbc.app.service.impl;

import com.bbc.app.dto.request.CreateCustomerRequest;
import com.bbc.app.dto.request.UpdateCustomerRequest;
import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.model.Customer;
import com.bbc.app.model.PaymentTransaction;
import com.bbc.app.model.User;
import com.bbc.app.model.UserRole;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.PaymentTransactionRepsitory;
import com.bbc.app.repository.UserRepository;
import com.bbc.app.service.CustomerService;
import com.bbc.app.utils.CustomerParsing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PaymentTransactionRepsitory paymentTransactionRepository;

    @Autowired
    private UserRepository userRepo;

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomerByMeterno(String meterNo) {
        Optional<Customer> customer = customerRepository.findByMeterNo(meterNo);
        return customer.orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public Customer createCustomer(List<CreateCustomerRequest> customerReq) {
        for(CreateCustomerRequest single :customerReq) {
            User user;
            user = userRepo.findByEmail(single.getEmail());

            if (ObjectUtils.isEmpty(user)) {
                user = new User();
                user.setRole(UserRole.valueOf(UserRole.CUSTOMER.name()));
                user.setPhone(single.getPhone());
                user.setName(single.getName());
                user.setEmail(single.getEmail());
                userRepo.save(user);
                Customer customer = new Customer();
                customer.setUser(user);
                customer.setAddress(single.getAddress());
                customerRepository.save(customer);
            }
        }
        return null;
    }

    @Override
    public Customer updateCustomer(String meterno, UpdateCustomerRequest customer) {
        Customer existingCustomer = getCustomerByMeterno(meterno);
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.getUser().setName(customer.getName());
        existingCustomer.getUser().setEmail(customer.getEmail());
        existingCustomer.getUser().setPhone(customer.getPhone());
        // Update other fields as necessary
        return customerRepository.save(existingCustomer);
    }

    @Override
    public String deleteCustomer(String meterno) {
        Customer customer = getCustomerByMeterno(meterno);
        if(!ObjectUtils.isEmpty(customer)) {
            customerRepository.delete(customer);
            return "Customer deleted successfully.";
        }
        return "Customer not found for this meter no:- " + meterno;
    }

    @Override
    public List<PaymentTransaction> getTransactionsForCustomer(String customerId){
        List<PaymentTransaction> byCustomerCustomerId = paymentTransactionRepository.findByCustomer_CustomerId(customerId);
        return byCustomerCustomerId;
    }

    @Override
    public ResponseEntity<MessageResponse> bulkUploadCustomer(MultipartFile dataFile) throws IOException {

        Map<String,Object> response = new HashMap<String, Object>();
        if (!dataFile.getOriginalFilename().endsWith(".csv")) {

            return ResponseEntity.badRequest().body(new MessageResponse("Error! Invalid file format"));
        }
        List<Customer> customers = CustomerParsing.parseCSV(dataFile.getInputStream());
        return ResponseEntity.ok(new MessageResponse("Data uploaded successfully."));

    }


}
