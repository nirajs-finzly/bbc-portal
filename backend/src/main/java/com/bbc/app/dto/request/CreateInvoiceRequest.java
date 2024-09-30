package com.bbc.app.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CreateInvoiceRequest {
    @NotBlank(message = "Meter No is required!")
    @Pattern(regexp = "^(MTR)\\d{7}$", message = "Invalid Meter No!")
    private String meterNo;

    @NotBlank(message = "Units Consumed is required!")
    private BigDecimal unitsConsumed;

    @NotBlank(message = "Bill Duration is required!")
    String billDuration;

    @NotBlank(message = "Bill Due Date is required!")
    LocalDate billDueDate;

    public CreateInvoiceRequest(String meterNo, BigDecimal unitsConsumed, String billDuration, LocalDate billDueDate) {
        this.meterNo = meterNo;
        this.unitsConsumed = unitsConsumed;
        this.billDuration = billDuration;
        this.billDueDate = billDueDate;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }

    public BigDecimal getUnitsConsumed() {
        return unitsConsumed;
    }

    public void setUnitsConsumed(BigDecimal unitsConsumed) {
        this.unitsConsumed = unitsConsumed;
    }

    public String getBillDuration() {
        return billDuration;
    }

    public void setBillDuration(String billDuration) {
        this.billDuration = billDuration;
    }

    public LocalDate getBillDueDate() {
        return billDueDate;
    }

    public void setBillDueDate(LocalDate billDueDate) {
        this.billDueDate = billDueDate;
    }
}
