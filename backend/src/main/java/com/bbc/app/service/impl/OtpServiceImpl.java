package com.bbc.app.service.impl;

import com.bbc.app.dto.response.MessageResponse;
import com.bbc.app.exception.InvalidOtpException;
import com.bbc.app.exception.UserNotFoundException;
import com.bbc.app.model.Customer;
import com.bbc.app.model.Employee;
import com.bbc.app.model.User;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.EmployeeRepository;
import com.bbc.app.service.EmailService;
import com.bbc.app.service.OtpService;
import com.bbc.app.utils.OtpGenerator;
import com.bbc.app.utils.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailService emailService;

    private static final int OTP_EXPIRY_MINUTES = 10;  // OTP expiry time in minutes

    @Override
    public ResponseEntity<MessageResponse> sendOtp(String userId) {
        // Check if the request rate is allowed (rate limiting)
        if (!RateLimiter.isAllowed(userId)) {
            throw new InvalidOtpException("Too many requests. Please try again later!");
        }

        // Generate the OTP
        String otp = OtpGenerator.generateOtp();

        User user;

        // Check if the user is an employee (starts with EMP) or customer (starts with CRN)
        if (userId.startsWith("EMP")) {
            // Find the employee by their ID
            Employee employee = employeeRepository.findByEmployeeId(userId)
                    .orElseThrow(() -> new UserNotFoundException("Employee not found"));

            user = employee.getUser();  // Assume Employee has a relationship with the User entity

        } else if (userId.startsWith("CRN")) {
            // Find the customer by their ID
            Customer customer = customerRepository.findByCustomerId(userId)
                    .orElseThrow(() -> new UserNotFoundException("Customer not found"));

            user = customer.getUser();  // Assume Customer has a relationship with the User entity

        } else {
            throw new UserNotFoundException("Invalid user ID format");
        }

        // Set OTP and OTP expiry time (current time + 10 minutes)
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));

        // Save the updated user (this assumes User is saved via Employee or Customer repositories)
        if (userId.startsWith("EMP")) {
            employeeRepository.save(user.getEmployee());  // Save through employee
        } else {
            customerRepository.save(user.getCustomer());  // Save through customer
        }

        // Send the OTP to the user's email
        emailService.sendOtpEmail(user.getEmail(), otp);

        // Return success message response
        return ResponseEntity.ok(new MessageResponse("OTP sent successfully to " + user.getEmail()));
    }
}