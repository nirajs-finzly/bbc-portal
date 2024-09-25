package com.bbc.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ops")
public class EmployeeController {
    @GetMapping("")
    public ResponseEntity<String> getEmployee() {
        return ResponseEntity.ok("Employee");
    }
}
