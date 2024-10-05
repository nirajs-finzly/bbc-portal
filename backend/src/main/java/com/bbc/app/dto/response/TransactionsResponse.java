package com.bbc.app.dto.response;

import com.bbc.app.dto.data.TransactionData;

import java.util.List;

public class TransactionsResponse {
    private String message;
    private List<TransactionData> transactions;
    private boolean success;

    public TransactionsResponse(String message, List<TransactionData> transactions, boolean success) {
        this.message = message;
        this.transactions = transactions;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TransactionData> getInvoices() {
        return transactions;
    }

    public void setInvoices(List<TransactionData> invoices) {
        this.transactions = invoices;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
