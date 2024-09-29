package com.bbc.app.service.impl;

import com.bbc.app.dto.response.UserData;
import com.bbc.app.dto.response.UserResponse;
import com.bbc.app.model.Customer;
import com.bbc.app.model.Employee;
import com.bbc.app.model.User;
import com.bbc.app.service.UserService;
import com.bbc.app.utils.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public ResponseEntity<UserResponse> getAuthenticatedUser() {
        User currentUser = SecurityUtils.getAuthenticatedUser();

        if (currentUser == null) {
            return ResponseEntity.status(403).body(new UserResponse(
                    "Unauthorized request!", null));
        }

        // Determine if the user is an Employee or Customer
        String role = currentUser.getRole().name();
        if (role.equals("EMPLOYEE") && currentUser.getEmployee() != null) {
            Employee employee = currentUser.getEmployee();

            UserData userData = new UserData(currentUser.getName(),
                    currentUser.getEmail(),
                    currentUser.getPhone(),
                    role,
                    employee.getDepartment(),
                    employee.getPosition(),
                    null, null
            );

            return ResponseEntity.ok(
                    new UserResponse("User data found!", userData)
            );
        } else if (role.equals("CUSTOMER") && currentUser.getCustomer() != null) {
            Customer customer = currentUser.getCustomer();

            UserData userData = new UserData(currentUser.getName(),
                    currentUser.getEmail(),
                    currentUser.getPhone(),
                    role,
                    null, null,
                    customer.getAddress(),
                    customer.getMeterNo()
            );

            return ResponseEntity.ok(
                    new UserResponse("User data found", userData)
            );
        }

        // If neither employee nor customer record is found
        return ResponseEntity.status(404).body(new UserResponse("User details not found!", null));
    }
}
