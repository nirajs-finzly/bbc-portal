package com.bbc.app.dto.response;

import com.bbc.app.dto.data.TransactionData;

import java.util.List;

public class TransactionsResponse {
    private String message;
    private List<TransactionData> transactions;
    private Long totalTransactions;
    private boolean success;

    public TransactionsResponse(String message, List<TransactionData> transactions, Long totalTransactions, boolean success) {
        this.message = message;
        this.transactions = transactions;
        this.totalTransactions = totalTransactions;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TransactionData> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionData> transactions) {
        this.transactions = transactions;
    }

    public Long getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}