package com.bank.management.dto.response;

import com.bank.management.entity.User;

import java.time.LocalDateTime;

/**
 * DTO for authentication response.
 *
 * This DTO contains the JWT token and user information
 * returned after successful authentication.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
public class AuthResponse {

    /**
     * JWT access token.
     */
    private String accessToken;

    /**
     * Token type (Bearer).
     */
    private String tokenType = "Bearer";

    /**
     * User ID.
     */
    private Long userId;

    /**
     * Username.
     */
    private String username;

    /**
     * User email.
     */
    private String email;

    /**
     * User full name.
     */
    private String fullName;

    /**
     * User role.
     */
    private User.Role role;

    /**
     * Token expiration time.
     */
    private LocalDateTime expiresAt;

    /**
     * Default constructor.
     */
    public AuthResponse() {
    }

    /**
     * Constructor with essential fields.
     *
     * @param accessToken the JWT token
     * @param user the authenticated user
     * @param expiresAt the token expiration time
     */
    public AuthResponse(String accessToken, User user, LocalDateTime expiresAt) {
        this.accessToken = accessToken;
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.role = user.getRole();
        this.expiresAt = expiresAt;
    }

    // Getters and Setters

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * Checks if the user has admin role.
     *
     * @return true if admin
     */
    public boolean isAdmin() {
        return role == User.Role.ADMIN;
    }

    /**
     * Checks if the user has customer role.
     *
     * @return true if customer
     */
    public boolean isCustomer() {
        return role == User.Role.CUSTOMER;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", expiresAt=" + expiresAt +
                '}';
    }
}