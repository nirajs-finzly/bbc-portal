package com.bbc.app.service.impl;

import com.bbc.app.model.Customer;
import com.bbc.app.model.Employee;
import com.bbc.app.model.User;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    public UserDetailsServiceImpl(EmployeeRepository employeeRepository, CustomerRepository customerRepository) {
        this.employeeRepository = employeeRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user;

        if (id.startsWith("EMP")) {
            user = employeeRepository.findByEmployeeId(id)
                    .map(Employee::getUser)
                    .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));
        } else if (id.startsWith("CRN")) {
            user = customerRepository.findByCustomerId(id)
                    .map(Customer::getUser)
                    .orElseThrow(() -> new UsernameNotFoundException("Customer not found"));
        } else {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }
}