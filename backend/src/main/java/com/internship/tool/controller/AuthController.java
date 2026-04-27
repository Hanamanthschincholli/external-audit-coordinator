package com.internship.tool.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.internship.tool.dto.LoginRequest;
import com.internship.tool.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        String role = "";

        if ("admin".equals(request.getUsername()) && "admin".equals(request.getPassword())) {
        role = "ADMIN";
        } else if ("user".equals(request.getUsername()) && "user".equals(request.getPassword())) {
        role = "USER";
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(request.getUsername() + ":" + role);
        return ResponseEntity.ok(token);
    }
}
