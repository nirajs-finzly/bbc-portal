package com.bbc.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OtpRequest {
    @NotBlank(message = "Id is required!")
    @Pattern(regexp = "^(EMP|CRN)\\d{7}$", message = "Invalid Id!")
    private String id;

    public OtpRequest() {}

    public OtpRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
