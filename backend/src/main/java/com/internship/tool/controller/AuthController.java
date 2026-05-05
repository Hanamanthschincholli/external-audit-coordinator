package com.internship.tool.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.internship.tool.dto.LoginRequest;
import com.internship.tool.security.JwtUtil;
import com.internship.tool.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(JwtUtil jwtUtil, UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {

        String loginInput = request.getUsername();

        return userService.findByUsername(loginInput)
                .map(user -> validatePasswordAndGenerateToken(user, request.getPassword()))
                .orElseGet(() -> userService.findByEmail(loginInput)
                        .map(user -> validatePasswordAndGenerateToken(user, request.getPassword()))
                        .orElse(ResponseEntity.status(401).body("User not found")));
    }

    private ResponseEntity<String> validatePasswordAndGenerateToken(
            com.internship.tool.entity.User user,
            String rawPassword) {

        String storedPassword = user.getPasswordHash();
        boolean matches = passwordEncoder.matches(rawPassword, storedPassword)
                || rawPassword.equals(storedPassword);

        if (!matches) {
            return ResponseEntity.status(401).body("Invalid password");
        }

        String subject = (user.getUsername() != null && !user.getUsername().isBlank())
                ? user.getUsername()
                : user.getEmail();

        String token = jwtUtil.generateToken(
                subject + ":" + user.getRole()
        );

        return ResponseEntity.ok(token);
    }
}

