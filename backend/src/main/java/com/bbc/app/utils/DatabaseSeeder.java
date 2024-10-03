package com.bbc.app.utils;

import com.bbc.app.model.*;
import com.bbc.app.repository.*;
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

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private DebitCardRepository debitCardRepository;

    @Autowired
    private NetBankingRepository netBankingRepository;

    @Autowired
    private UPIRepository upiRepository;

    @Override
    public void run(String... args) {
        seedData();
    }

    private void seedData() {
        // Check if user table is empty
        if (userRepository.count() == 0) {
            // Create and save the user
            User user1 = new User();
            user1.setName("Niraj Surve");
            user1.setEmail("niraj.surve07@gmail.com");
            user1.setPhone("1234567890");
            user1.setRole(UserRole.EMPLOYEE);
            userRepository.save(user1);

            // Create and save the employee2 associated with the user
            Employee employee = new Employee();
            employee.setUser(user1);
            employee.setDepartment("Operations");
            employee.setPosition("Sub-Officer");
            employeeRepository.save(employee);

            User user3 = new User();
            user3.setName("Sushmita Bennisur");
            user3.setEmail("sushmitabennisur11@gmail.com");
            user3.setPhone("7744920605");
            user3.setRole(UserRole.EMPLOYEE);
            userRepository.save(user3);

            // Create and save the employee2 associated with the user
            Employee employee2 = new Employee();
            employee2.setUser(user3);
            employee2.setDepartment("Operations");
            employee2.setPosition("Sub-Officer");
            employeeRepository.save(employee2);

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

            User user4 = new User();
            user4.setName("Sakshi Dhumal");
            user4.setEmail("sakshi@gmail.com");
            user4.setPhone("1234567890");
            user4.setRole(UserRole.CUSTOMER);
            userRepository.save(user4);

            Customer customer2 = new Customer();
            customer2.setUser(user4);
            customer2.setAddress("Bhor");
            customerRepository.save(customer2);

            Invoice invoice = new Invoice();
            invoice.setCustomer(customer);
            invoice.setUnitConsumption(BigDecimal.valueOf(30));
            invoice.setBillDuration("September 2024");
            invoice.setBillDueDate(LocalDate.parse("2024-10-10"));
            invoice.setCurrentAmountDue(BigDecimal.valueOf(1000.00));
            invoice.setTotalAmountDue(BigDecimal.valueOf(1000.00));
            invoiceRepository.save(invoice);

            // Seed a credit card
            CreditCard creditCard = new CreditCard();
            creditCard.setCardNumber("1234567812345678");
            creditCard.setExpiryDate("12/25");
            creditCard.setCvv("123");
            creditCard.setBalance(BigDecimal.valueOf(10000.00));
            creditCardRepository.save(creditCard);

            // Seed a debit card
            DebitCard debitCard = new DebitCard();
            debitCard.setCardNumber("8765432187654321");
            debitCard.setExpiryDate("11/26");
            debitCard.setCvv("456");
            debitCard.setBalance(BigDecimal.valueOf(10000.00));
            debitCardRepository.save(debitCard);

            // Seed net banking
            NetBanking netBanking = new NetBanking();
            netBanking.setBankName("BOI");
            netBanking.setAccountNumber("123456789012");
            netBanking.setIfscCode("BKID1234567");
            netBanking.setBalance(BigDecimal.valueOf(10000.00));
            netBankingRepository.save(netBanking);

            // Seed UPI
            UPI upi = new UPI();
            upi.setUpiIdValue("niraj@okaxis");
            upi.setBalance(BigDecimal.valueOf(10000.00));
            upiRepository.save(upi);

            System.out.println("Data seeding completed.");
        } else {
            System.out.println("User data already exists. Skipping seeding.");
        }
    }
}