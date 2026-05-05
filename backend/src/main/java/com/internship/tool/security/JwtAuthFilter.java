package com.internship.tool.security;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.internship.tool.service.UserService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    public JwtAuthFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        logger.debug("JWT Filter - Authorization Header received: {}", authHeader);
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Handle "Bearer Bearer token" (double prefix) or "Bearer token"
            String token = authHeader.substring(7);
            if (token.startsWith("Bearer ")) {
                // Double prefix case - remove the second "Bearer "
                token = token.substring(7);
                logger.debug("Detected double Bearer prefix, corrected token length: {}", token.length());
            }
            logger.debug("JWT Token extracted, length: {}", token.length());
            
            try {
                Claims claims = jwtUtil.validateToken(token);
                if (claims != null) {
                    String subject = claims.getSubject();
                    logger.debug("JWT subject: {}", subject);
                    
                    String[] parts = subject.split(":");
                    if (parts.length == 2) {
                        String username = parts[0];
                        String tokenRole = parts[1];
                        logger.debug("Extracted username: {}, role: {}", username, tokenRole);
                        
userService.findByUsername(username).ifPresent(user -> {
                            if (tokenRole.equals(user.getRole())) {
                                List<SimpleGrantedAuthority> authorities = 
                                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
                                Authentication auth = new UsernamePasswordAuthenticationToken(
                                    user, null, authorities);
                                SecurityContextHolder.getContext().setAuthentication(auth);
                                logger.info("Authentication set for user: {} with role: {}", 
                                    username, user.getRole());
                            }
                        });
                    }
                }
            } catch (Exception e) {
                logger.warn("JWT validation failed: {}", e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
