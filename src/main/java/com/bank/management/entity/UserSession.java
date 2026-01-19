package com.bank.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * UserSession entity for tracking user login sessions and security auditing.
 *
 * This entity maintains session information for security monitoring,
 * concurrent session management, and audit trails.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
@Entity
@Table(name = "user_sessions", indexes = {
    @Index(name = "idx_session_user_id", columnList = "user_id"),
    @Index(name = "idx_session_token", columnList = "session_token", unique = true),
    @Index(name = "idx_session_is_active", columnList = "is_active"),
    @Index(name = "idx_session_login_datetime", columnList = "login_datetime")
})
public class UserSession {

    /**
     * Unique identifier for the session.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The user associated with this session.
     */
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Unique session token for the session.
     */
    @NotBlank(message = "Session token is required")
    @Size(max = 255, message = "Session token must not exceed 255 characters")
    @Column(name = "session_token", nullable = false, unique = true, length = 255)
    private String sessionToken;

    /**
     * IP address from which the session was initiated.
     */
    @Size(max = 45, message = "IP address must not exceed 45 characters")
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    /**
     * User agent string from the client browser/device.
     */
    @Size(max = 500, message = "User agent must not exceed 500 characters")
    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    /**
     * Date and time when the session was initiated.
     */
    @NotNull(message = "Login datetime is required")
    @Column(name = "login_datetime", nullable = false)
    private LocalDateTime loginDateTime;

    /**
     * Date and time when the session was terminated.
     */
    @Column(name = "logout_datetime")
    private LocalDateTime logoutDateTime;

    /**
     * Flag indicating if the session is currently active.
     */
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    /**
     * Timestamp when the session record was created.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor.
     */
    public UserSession() {
    }

    /**
     * Constructor with essential fields.
     *
     * @param user the user
     * @param sessionToken the session token
     * @param ipAddress the IP address
     * @param userAgent the user agent
     */
    public UserSession(User user, String sessionToken, String ipAddress, String userAgent) {
        this.user = user;
        this.sessionToken = sessionToken;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.loginDateTime = LocalDateTime.now();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public LocalDateTime getLoginDateTime() {
        return loginDateTime;
    }

    public void setLoginDateTime(LocalDateTime loginDateTime) {
        this.loginDateTime = loginDateTime;
    }

    public LocalDateTime getLogoutDateTime() {
        return logoutDateTime;
    }

    public void setLogoutDateTime(LocalDateTime logoutDateTime) {
        this.logoutDateTime = logoutDateTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Marks the session as inactive and sets logout time.
     */
    public void logout() {
        this.isActive = false;
        this.logoutDateTime = LocalDateTime.now();
    }

    /**
     * Checks if the session is expired based on a timeout period.
     *
     * @param timeoutMinutes the timeout in minutes
     * @return true if session is expired
     */
    public boolean isExpired(int timeoutMinutes) {
        if (!isActive) {
            return true;
        }
        LocalDateTime expiryTime = loginDateTime.plusMinutes(timeoutMinutes);
        return LocalDateTime.now().isAfter(expiryTime);
    }

    /**
     * Gets the session duration in minutes.
     *
     * @return the duration in minutes
     */
    public long getSessionDurationMinutes() {
        LocalDateTime endTime = logoutDateTime != null ? logoutDateTime : LocalDateTime.now();
        return java.time.Duration.between(loginDateTime, endTime).toMinutes();
    }

    /**
     * Gets the username for display purposes.
     *
     * @return the username
     */
    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

    @Override
    public String toString() {
        return "UserSession{" +
                "id=" + id +
                ", sessionToken='" + sessionToken + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", loginDateTime=" + loginDateTime +
                ", isActive=" + isActive +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSession)) return false;
        UserSession that = (UserSession) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}