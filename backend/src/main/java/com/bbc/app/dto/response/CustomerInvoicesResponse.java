package com.bbc.app.dto.response;

import com.bbc.app.dto.data.CustomerInvoiceData;

import java.util.List;

public class CustomerInvoicesResponse {
    private String message;
    private List<CustomerInvoiceData> invoices;
    private Long totalInvoices;
    private boolean success;

    public CustomerInvoicesResponse(String message, List<CustomerInvoiceData> invoices, Long totalInvoices, boolean success) {
        this.message = message;
        this.invoices = invoices;
        this.totalInvoices = totalInvoices;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CustomerInvoiceData> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<CustomerInvoiceData> invoices) {
        this.invoices = invoices;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getTotalInvoices() {
        return totalInvoices;
    }

    public void setTotalInvoices(Long totalInvoices) {
        this.totalInvoices = totalInvoices;
    }
}
