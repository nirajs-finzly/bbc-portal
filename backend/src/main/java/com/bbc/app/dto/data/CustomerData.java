package com.bbc.app.dto.data;

public class CustomerData {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String meterNo;

    public CustomerData(String name, String email, String phone, String address, String meterNo) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.meterNo = meterNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMeterNo() {
        return meterNo;
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
    }
}
