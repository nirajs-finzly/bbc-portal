package com.bbc.app.dto.response;

public class PaymentResponse {
    private String message;
    private String callbackUrl;
    private String redirectUrl;
    private boolean success;

    public PaymentResponse(String message, String callbackUrl, String redirectUrl, boolean success) {
        this.message = message;
        this.callbackUrl = callbackUrl;
        this.redirectUrl = redirectUrl;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
