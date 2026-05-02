package com.internship.tool.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET = "my-secret-key-my-secret-key-my-secret-key-my-secret-key";
    private static final long EXPIRATION_MS = 86400000; // 1 day

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String generateToken(String data) {
        return Jwts.builder()
                .subject(data)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key)
                .compact();
    }

public Claims validateToken(String token) {
        try {
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
}
