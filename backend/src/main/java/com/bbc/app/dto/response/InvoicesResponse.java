package com.bbc.app.dto.response;

import com.bbc.app.dto.data.InvoiceData;

import java.util.List;

public class InvoicesResponse {
    private String message;
    private List<InvoiceData> invoices;
    private Long totalInvoices;
    private boolean success;

    public InvoicesResponse(String message, List<InvoiceData> invoices, Long totalInvoices, boolean success) {
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

    public List<InvoiceData> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<InvoiceData> invoices) {
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
