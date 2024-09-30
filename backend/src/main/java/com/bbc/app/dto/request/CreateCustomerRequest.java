package com.bbc.app.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CreateCustomerRequest {

    @NotBlank(message = "name is required!")
    private String name;

    @NotBlank(message = "phone is required!")
    private String phone;

    @NotBlank(message = "email is required!")
    private String email;

    @NotBlank(message = "address is required!")
    private String address;

    public CreateCustomerRequest(String name, String phone, String email, String address) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public @NotBlank(message = "name is required!") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "name is required!") String name) {
        this.name = name;
    }

    public @NotBlank(message = "email is required!") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "email is required!") String email) {
        this.email = email;
    }

    public @NotBlank(message = "phone is required!") String getPhone() {
        return phone;
    }

    public void setPhone(@NotBlank(message = "phone is required!") String phone) {
        this.phone = phone;
    }

    public @NotBlank(message = "address is required!") String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank(message = "address is required!") String address) {
        this.address = address;
    }
}
