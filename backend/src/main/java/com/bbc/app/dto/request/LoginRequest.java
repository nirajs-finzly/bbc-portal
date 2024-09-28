package com.bbc.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoginRequest {
    @NotBlank(message = "Id is required!")
    @Pattern(regexp = "^(EMP|CRN)\\d{7}$", message = "Invalid Id!")
    private String identifier;

    @NotBlank(message = "OTP is required!")
    @Pattern(regexp = "^\\d{6}$", message = "OTP must be a 6-digit number!")
    private String otp;

    public LoginRequest(String identifier, String otp) {
        this.identifier = identifier;
        this.otp = otp;
    }

    public @NotBlank(message = "Id is required!") @Pattern(regexp = "^(EMP|CRN)\\d{7}$", message = "Invalid Id!") String getIdentifier() {
        return identifier;
    }

    public @NotBlank(message = "OTP is required!") @Pattern(regexp = "^\\d{6}$", message = "OTP must be a 6-digit number!") String getOtp() {
        return otp;
    }
}