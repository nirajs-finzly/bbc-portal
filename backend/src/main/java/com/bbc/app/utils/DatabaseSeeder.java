package com.bbc.app.utils;

import com.bbc.app.model.*;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.EmployeeRepository;
import com.bbc.app.repository.InvoiceRepository;
import com.bbc.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public void run(String... args) {
        seedEmployeeUsers();
    }

    private void seedEmployeeUsers() {
        // Check if user table is empty
        if (userRepository.count() == 0) {
            // Create and save the user
            User user1 = new User();
            user1.setName("Niraj Surve");
            user1.setEmail("niraj.surve07@gmail.com");
            user1.setPhone("1234567890");
            user1.setRole(UserRole.EMPLOYEE);
            userRepository.save(user1);

            // Create and save the employee associated with the user
            Employee employee = new Employee();
            employee.setUser(user1);
            employee.setDepartment("Operations");
            employee.setPosition("Sub-Officer");
            employeeRepository.save(employee);

            User user2 = new User();
            user2.setName("Sujal Surve");
            user2.setEmail("webdeveloper.nirajs@gmail.com");
            user2.setPhone("1234567890");
            user2.setRole(UserRole.CUSTOMER);
            userRepository.save(user2);

            Customer customer = new Customer();
            customer.setUser(user2);
            customer.setAddress("Mumbai");
            customerRepository.save(customer);

            Invoice invoice1 = new Invoice();
            invoice1.setCustomer(customer);
            invoice1.setUnitConsumption(new BigDecimal("250.75"));
            invoice1.setBillDuration("January 2024");
            invoice1.setBillDueDate(LocalDate.of(2024, 2, 10));
            invoice1.setAmountDue(new BigDecimal("1250.50"));
            invoiceRepository.save(invoice1);

            Invoice invoice2 = new Invoice();
            invoice2.setCustomer(customer);
            invoice2.setUnitConsumption(new BigDecimal("300.25"));
            invoice2.setBillDuration("February 2024");
            invoice2.setBillDueDate(LocalDate.of(2024, 3, 10));
            invoice2.setAmountDue(new BigDecimal("1500.75"));
            invoiceRepository.save(invoice2);
            User user3 = new User();
            user3.setName("Sushmita Bennisur");
            user3.setEmail("sushmitabennisur11@gmail.com");
            user3.setPhone("7744920605");
            user3.setRole(UserRole.EMPLOYEE);
            userRepository.save(user3);

            Employee employee2 = new Employee();
            employee2.setUser(user3);
            employee2.setDepartment("Operations");
            employee2.setPosition("Sub-Officer");
            employeeRepository.save(employee2);

            System.out.println("Data seeding completed.");
        } else {
            System.out.println("User data already exist. Skipping seeding.");
        }
    }
}