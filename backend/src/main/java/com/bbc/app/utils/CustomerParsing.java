package com.bbc.app.utils;

import com.bbc.app.model.Customer;
import com.bbc.app.model.User;
import com.bbc.app.model.UserRole;
import com.bbc.app.service.CustomerService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomerParsing {

    @Autowired
    private CustomerService customerService;

    public static List<Customer> parseCSV(InputStream inputStream) throws IOException {
        List<Customer> customerList = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try (InputStreamReader reader = new InputStreamReader(inputStream);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {

                String name = csvRecord.get("Name");
                String email = csvRecord.get("Email");
                String phoneNumber = csvRecord.get("PhoneNumber");
                String address = csvRecord.get("Address");


                // Validate name (not null or empty)
                if (name == null || name.isBlank()) {

                    continue; // Skip this record and continue with the next one
                }

                // Validate email ( email validation)
                if (email == null ||email.isBlank()|| !email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")) {

                    continue; // Skip this record and continue with the next one
                }

                // Validate telephone number
                if (phoneNumber == null ||phoneNumber.isBlank()||phoneNumber.isBlank()||!phoneNumber.matches("\\d{10}")) {

                    continue; // Skip this record and continue with the next one
                }

//                customerService.createCustomer(csvRecord);

                // Create a CustomerData object and add it to the list
                Customer customer = new Customer();
                customer.getUser().setName(name);
                customer.getUser().setEmail(email);
                customer.getUser().setPhone(phoneNumber);
                customer.setAddress(address);
                customerList.add(customer);
            }
        }

        return customerList;
    }
}
