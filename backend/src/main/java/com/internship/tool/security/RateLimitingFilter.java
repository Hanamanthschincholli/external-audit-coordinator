package com.internship.tool.security;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Order(Ordered.HIGHEST_PRECEDENCE + 50)
@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS = 60;
    private static final long WINDOW_SECONDS = 60;

    private static class Counter {
        int count;
        long windowStart;
    }

    private final Map<String, Counter> requestCounts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();
        long now = Instant.now().getEpochSecond();

        Counter counter = requestCounts.computeIfAbsent(clientIp, ip -> {
            Counter c = new Counter();
            c.count = 0;
            c.windowStart = now;
            return c;
        });

        synchronized (counter) {
            if (now - counter.windowStart >= WINDOW_SECONDS) {
                counter.count = 0;
                counter.windowStart = now;
            }

            counter.count++;

            if (counter.count > MAX_REQUESTS) {
                response.setStatus(429);
                response.setContentType("application/json");
                response.getWriter().write(
                        "{\"message\":\"Too many requests. Try again later.\",\"status\":429}"
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
