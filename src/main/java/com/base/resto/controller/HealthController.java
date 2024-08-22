package com.base.resto.controller;

import com.base.resto.dto.response.AuthenticationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/admin/health")
    public ResponseEntity<String> adminHealth() {
        try {
            return ResponseEntity.ok("Admin health OK");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/employee/health")
    public ResponseEntity<String> employeeHealth() {
        try{
            return ResponseEntity.ok("Employee health OK");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }

    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        try{
            return ResponseEntity.ok("Health OK");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
