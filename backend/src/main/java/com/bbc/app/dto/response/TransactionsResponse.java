package com.bbc.app.dto.response;

import com.bbc.app.dto.data.TransactionData;

import java.util.List;

public class TransactionsResponse {
    private String message;
    private List<TransactionData> invoices;
    private boolean success;

    public TransactionsResponse(String message, List<TransactionData> invoices, boolean success) {
        this.message = message;
        this.invoices = invoices;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TransactionData> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<TransactionData> invoices) {
        this.invoices = invoices;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
