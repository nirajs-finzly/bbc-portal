package com.bbc.app.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateCustomerRequest {

    @NotBlank(message = "name is required!")
    private String name;

    @NotBlank(message = "email is required!")
    private String email;

    @NotBlank(message = "phone is required!")
    private String phone;

    @NotBlank(message = "address is required!")
    private String address;

    public CreateCustomerRequest() {
    }

    public CreateCustomerRequest(String name, String address, String phone, String email) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
