package com.bbc.app.dto.request;

import java.util.UUID;

public class InitiatePaymentRequest {
    private String meterNo;
    private UUID invoiceId;

    public InitiatePaymentRequest(String meterNo, UUID invoiceId) {
        this.meterNo = meterNo;
        this.invoiceId = invoiceId;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }
}