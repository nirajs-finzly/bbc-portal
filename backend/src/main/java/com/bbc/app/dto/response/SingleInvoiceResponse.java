package com.bbc.app.dto.response;

import com.bbc.app.dto.data.InvoiceData;

public class SingleInvoiceResponse {
    private String message;
    private InvoiceData invoiceData;
    private boolean success;

    public SingleInvoiceResponse(String message, InvoiceData invoiceData, boolean success) {
        this.message = message;
        this.invoiceData = invoiceData;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public InvoiceData getInvoiceData() {
        return invoiceData;
    }

    public void setInvoiceData(InvoiceData invoiceData) {
        this.invoiceData = invoiceData;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
