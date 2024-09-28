package com.bbc.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OtpRequest {
    @NotBlank(message = "Id is required!")
    @Pattern(regexp = "^(EMP|CRN)\\d{7}$", message = "Invalid Id!")
    private String identifier;

    public OtpRequest() {}

    public OtpRequest(String identifier) {
        this.identifier = identifier;
    }

    public @NotBlank(message = "Id is required!") @Pattern(regexp = "^(EMP|CRN)\\d{7}$", message = "Invalid Id!") String getIdentifier() {
        return identifier;
    }
}
