package com.bbc.app.dto.request;

import jakarta.validation.constraints.NotBlank;

public class UpdateCustomerRequest {
    @NotBlank(message = "name is required!")
    private String name;

    @NotBlank(message = "email is required!")
    private String phone;

    @NotBlank(message = "address is required!")
    private String address;

    public UpdateCustomerRequest(String name, String phone, String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public @NotBlank(message = "name is required!") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "name is required!") String name) {
        this.name = name;
    }

    public @NotBlank(message = "email is required!") String getPhone() {
        return phone;
    }

    public void setPhone(@NotBlank(message = "email is required!") String phone) {
        this.phone = phone;
    }

    public @NotBlank(message = "address is required!") String getAddress() {
        return address;
    }

    public void setAddress(@NotBlank(message = "address is required!") String address) {
        this.address = address;
    }
}
