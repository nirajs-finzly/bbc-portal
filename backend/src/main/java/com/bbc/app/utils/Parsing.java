package com.bbc.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.bbc.app.model.Customer;
import com.bbc.app.model.User;
import com.bbc.app.model.UserRole;
import com.bbc.app.repository.UserRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Parsing {

    @Autowired
    private UserRepository userRepository;

    public List<Customer> parseCSV(InputStream inputStream, AtomicInteger invalidRecords) throws IOException {
        List<Customer> customerList = new ArrayList<>();


        try (InputStreamReader reader = new InputStreamReader(inputStream);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {

                String name = csvRecord.get("Name");
                String email = csvRecord.get("Email");
                String phone = csvRecord.get("Phone");
                String address = csvRecord.get("Address");


                // Validate name (not null or empty)
                if (name == null || name.isEmpty()) {
                    invalidRecords.incrementAndGet();
                    continue; // Skip this record and continue with the next one
                }

                // Validate email ( email validation)
                if (email == null ||email.isEmpty()|| !email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")) {
                    Optional<User> customer = userRepository.findByEmail(email);
                    if(customer.isPresent()){
                        invalidRecords.incrementAndGet();
                        continue; // Skip this record and continue with the next one
                    }
                }

                // Validate phone number
                if (phone == null ||phone.isEmpty()||phone.isEmpty()||!phone.matches("\\d{10}")) {
                    invalidRecords.incrementAndGet();
                    continue; // Skip this record and continue with the next one
                }

                Optional<User> existingUser = userRepository.findByEmail(email);
                if (!existingUser.isEmpty()) {
                    // Skip existing users
                    invalidRecords.incrementAndGet();
                    continue;
                }

                User user = new User();
                user.setName(name);
                user.setEmail(email);
                user.setPhone(phone);
                user.setRole(UserRole.CUSTOMER);
                // Create a Customer object and add it to the list
                Customer customer = new Customer();
                customer.setUser(user);
                customer.setAddress(address);
                // Set other fields as needed

                customerList.add(customer);
            }
        }

        return customerList;
    }


}

