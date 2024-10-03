package com.bbc.app.utils;

import com.bbc.app.model.Customer;
import com.bbc.app.model.CreditCard;
import com.bbc.app.model.DebitCard;
import com.bbc.app.model.Employee;
import com.bbc.app.model.Invoice;
import com.bbc.app.model.NetBanking;
import com.bbc.app.model.UPI;
import com.bbc.app.model.User;
import com.bbc.app.model.UserRole;
import com.bbc.app.repository.CustomerRepository;
import com.bbc.app.repository.CreditCardRepository;
import com.bbc.app.repository.DebitCardRepository;
import com.bbc.app.repository.EmployeeRepository;
import com.bbc.app.repository.InvoiceRepository;
import com.bbc.app.repository.NetBankingRepository;
import com.bbc.app.repository.UPIRepository;
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

            // Create and save the employee associated with the user
            Employee employee = new Employee();
            employee.setUser(user1);
            employee.setDepartment("Operations");
            employee.setPosition("Sub-Officer");
            employeeRepository.save(employee);

            // Create and save a customer
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

            // Create and save an invoice for the customer
            Invoice invoice = new Invoice();
            invoice.setCustomer(customer);
            invoice.setUnitConsumption(BigDecimal.valueOf(30));
            invoice.setBillDueDate(LocalDate.parse("2024-10-10"));
            invoice.setCurrentAmountDue(BigDecimal.valueOf(1000.00));
            invoice.setTotalAmountDue(BigDecimal.valueOf(1000.00));
            invoiceRepository.save(invoice);

            // Seed a credit card
            CreditCard creditCard = new CreditCard();
            creditCard.setCardNumber("1234567812345678");
            creditCard.setExpiryDate("12/25");
            creditCard.setCvv("123");
            creditCardRepository.save(creditCard);

            // Seed a debit card
            DebitCard debitCard = new DebitCard();
            debitCard.setCardNumber("8765432187654321");
            debitCard.setExpiryDate("11/26");
            debitCard.setCvv("456");
            debitCardRepository.save(debitCard);

            // Seed net banking
            NetBanking netBanking = new NetBanking();
            netBanking.setBankName("BOI");
            netBanking.setAccountNumber("123456789012");
            netBanking.setIfscCode("BKID1234567");
            netBankingRepository.save(netBanking);

            // Seed UPI
            UPI upi = new UPI();
            upi.setUpiIdValue("niraj@okaxis");
            upiRepository.save(upi);

            System.out.println("Data seeding completed.");
        } else {
            System.out.println("User data already exists. Skipping seeding.");
        }
    }
}