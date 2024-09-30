package com.bbc.app.dto.response;

import com.bbc.app.dto.data.CustomerData;

import java.util.List;

public class CustomersResponse {
    private String message;
    private List<CustomerData> customers;
    private boolean success;

    public CustomersResponse(String message, List<CustomerData> customers, boolean success) {
        this.message = message;
        this.customers = customers;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CustomerData> getCustomers() {
        return customers;
    }

    public void setCustomers(List<CustomerData> customers) {
        this.customers = customers;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
