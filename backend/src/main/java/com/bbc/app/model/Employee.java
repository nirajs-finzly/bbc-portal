package com.bbc.app.model;

import jakarta.persistence.*;

import java.util.Random;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @Column(name = "employee_id", length = 15, unique = true)
    private String employeeId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(length = 100)
    private String department;

    @Column(length = 100)
    private String position;

    public Employee() {
    }

    public Employee(String employeeId, User user, String department, String position) {
        this.employeeId = employeeId;
        this.user = user;
        this.department = department;
        this.position = position;
    }

    @PrePersist
    private void generateEmployeeId() {
        if (this.employeeId == null || this.employeeId.isEmpty()) {
            this.employeeId = "EMP" + generateRandomNumber();
        }
    }

    private String generateRandomNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000000) + 1000000; // Ensures 7 digits
        return String.valueOf(randomNumber);
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
