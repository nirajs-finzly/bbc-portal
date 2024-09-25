package com.bbc.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoginRequest {
    @NotBlank(message = "Id is required!")
    @Pattern(regexp = "^(EMP|CRN)\\d{7}$", message = "Invalid Id!")
    private String id;

    @NotBlank(message = "OTP is required!")
    @Pattern(regexp = "^\\d{6}$", message = "OTP must be a 6-digit number!")
    private String otp;

    public LoginRequest(String id, String otp) {
        this.id = id;
        this.otp = otp;
    }

    public String getId() {
        return id;
    }

    public String getOtp() {
        return otp;
    }
}