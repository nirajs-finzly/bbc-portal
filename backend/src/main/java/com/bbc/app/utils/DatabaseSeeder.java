package com.bbc.app.utils;

import com.bbc.app.model.Customer;
import com.bbc.app.model.Employee;
import com.bbc.app.model.User;
import com.bbc.app.model.UserRole;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.EmployeeRepository;
import com.bbc.app.repository.InvoiceRepository;
import com.bbc.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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

            System.out.println("Data seeding completed.");
        } else {
            System.out.println("User data already exist. Skipping seeding.");
        }
    }
}