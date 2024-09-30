package com.bbc.app.dto.response;

import com.bbc.app.model.Customer;

public class SingleCustomerResponse {
    private String message;
    private Customer customer;
    private boolean success;

    public SingleCustomerResponse(String message, Customer customer, boolean success) {
        this.message = message;
        this.customer = customer;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
