package com.internship.tool.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET:fallback-secret-for-dev-THIS-IS-WEAK-FALLBACK-CHANGE-ME}")
    private String secretKeyStr;

    private static final long EXPIRATION_MS = 86400000L; // 1 day

    public String generateToken(String data) {
        SecretKey key = Keys.hmacShaKeyFor(secretKeyStr.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(data)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secretKeyStr.getBytes(StandardCharsets.UTF_8));
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    public String extractRole(String token) {
        Claims claims = validateToken(token);
        if (claims == null) {
            return null;
        }
        String subject = claims.getSubject();
        if (subject == null) {
            return null;
        }
        int colonIndex = subject.indexOf(':');
        if (colonIndex == -1) {
            return null;
        }
        return subject.substring(colonIndex + 1);
    }

    public String extractUserId(String token) {
        Claims claims = validateToken(token);
        if (claims == null) {
            return null;
        }
        String subject = claims.getSubject();
        if (subject == null) {
            return null;
        }
        String[] parts = subject.split(":");
        if (parts.length >= 1) {
            return parts[0];
        }
        return null;
    }
}
