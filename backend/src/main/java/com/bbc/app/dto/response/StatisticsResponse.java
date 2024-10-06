package com.bbc.app.dto.response;

import com.bbc.app.dto.data.StatisticsData;

public class StatisticsResponse {
    private String message;
    private Long count;
    private StatisticsData data;
    private boolean success;

    public StatisticsResponse(String message, Long count, StatisticsData data, boolean success) {
        this.message = message;
        this.count = count;
        this.data = data;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public StatisticsData getData() {
        return data;
    }

    public void setData(StatisticsData data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
