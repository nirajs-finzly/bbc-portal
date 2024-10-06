package com.bbc.app.dto.data;

import java.math.BigDecimal;
import java.util.Map;

public class StatisticsData {
    private BigDecimal amount;
    private BigDecimal averageUnitConsumption;
    private Map<String, BigDecimal> consumptionData;
    private Map<String, Long> invoiceStatusData;

    public StatisticsData() {
    }

    public StatisticsData(BigDecimal amount, BigDecimal averageUnitConsumption, Map<String, BigDecimal> consumptionData, Map<String, Long> invoiceStatusData) {
        this.amount = amount;
        this.averageUnitConsumption = averageUnitConsumption;
        this.consumptionData = consumptionData;
        this.invoiceStatusData = invoiceStatusData;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAverageUnitConsumption() {
        return averageUnitConsumption;
    }

    public void setAverageUnitConsumption(BigDecimal averageUnitConsumption) {
        this.averageUnitConsumption = averageUnitConsumption;
    }

    public Map<String, BigDecimal> getConsumptionData() {
        return consumptionData;
    }

    public void setConsumptionData(Map<String, BigDecimal> consumptionData) {
        this.consumptionData = consumptionData;
    }

    public Map<String, Long> getInvoiceStatusData() {
        return invoiceStatusData;
    }

    public void setInvoiceStatusData(Map<String, Long> invoiceStatusData) {
        this.invoiceStatusData = invoiceStatusData;
    }
}
