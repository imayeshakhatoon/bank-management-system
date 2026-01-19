package com.bank.management.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        log.warn("Unauthorized access attempt to: {} - Reason: {}",
                request.getRequestURI(),
                authException.getMessage());

        // Set response status
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Create error response
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("success", false);
        errorDetails.put("timestamp", LocalDateTime.now());
        errorDetails.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        errorDetails.put("error", "Unauthorized");
        errorDetails.put("message", "Authentication is required to access this resource");
        errorDetails.put("path", request.getRequestURI());

        // Add specific error details based on exception type
        if (authException.getMessage() != null) {
            if (authException.getMessage().contains("JWT")) {
                errorDetails.put("errorCode", "JWT_TOKEN_INVALID");
                errorDetails.put("details", "Invalid or expired JWT token");
            } else if (authException.getMessage().contains("credentials")) {
                errorDetails.put("errorCode", "INVALID_CREDENTIALS");
                errorDetails.put("details", "Invalid username or password");
            } else {
                errorDetails.put("errorCode", "AUTHENTICATION_FAILED");
                errorDetails.put("details", authException.getMessage());
            }
        }

        // Write error response
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
        response.getWriter().flush();
    }
}