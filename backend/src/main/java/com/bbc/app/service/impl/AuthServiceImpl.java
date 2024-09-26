package com.bbc.app.service.impl;

import com.bbc.app.dto.request.LoginRequest;
import com.bbc.app.dto.response.LoginResponse;
import com.bbc.app.exception.InvalidOtpException;
import com.bbc.app.exception.UserNotFoundException;
import com.bbc.app.model.Customer;
import com.bbc.app.model.Employee;
import com.bbc.app.model.User;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.EmployeeRepository;
import com.bbc.app.repository.UserRepository;
import com.bbc.app.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtServiceImpl jwtService;

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        // Find user (either Employee or Customer) and get the associated User entity
        User user = findUserById(request.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        // Validate OTP and expiry
        boolean isValid = validateOtp(request.getOtp(), user);

        if (!isValid) {
            throw new InvalidOtpException("Invalid OTP!");
        }

        // Reset OTP and OTP expiry after successful validation
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        // Generate JWT token
        String token = jwtService.generateToken(user);

        // Return successful login response with the token (no cookie)
        return ResponseEntity.ok()
                .body(new LoginResponse("Login Successful!", token));
    }

    // Method to find the User based on the ID (either Employee or Customer)
    private Optional<User> findUserById(String id) {
        Optional<Employee> employeeOpt = employeeRepository.findByEmployeeId(id);
        if (employeeOpt.isPresent()) {
            return Optional.of(employeeOpt.get().getUser());
        }

        Optional<Customer> customerOpt = customerRepository.findByCustomerId(id);
        return customerOpt.map(Customer::getUser);
    }

    // Method to validate OTP and its expiry
    private boolean validateOtp(String inputOtp, User user) {
        if (user.getOtp() == null || !user.getOtp().equals(inputOtp)) {
            throw new InvalidOtpException("Invalid OTP!");
        }

        // Check if OTP is expired
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidOtpException("OTP has expired!");
        }

        return true;
    }
}