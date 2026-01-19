package com.bank.management.dto.response;

import com.bank.management.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for user response.
 *
 * This DTO provides user information for API responses,
 * excluding sensitive data like passwords.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
public class UserResponse {

    /**
     * User ID.
     */
    private Long id;

    /**
     * Username.
     */
    private String username;

    /**
     * Email address.
     */
    private String email;

    /**
     * First name.
     */
    private String firstName;

    /**
     * Last name.
     */
    private String lastName;

    /**
     * Phone number.
     */
    private String phoneNumber;

    /**
     * Date of birth.
     */
    private LocalDate dateOfBirth;

    /**
     * Address.
     */
    private String address;

    /**
     * User role.
     */
    private User.Role role;

    /**
     * Account active status.
     */
    private Boolean isActive;

    /**
     * Account locked status.
     */
    private Boolean isAccountLocked;

    /**
     * Last login timestamp.
     */
    private LocalDateTime lastLoginDateTime;

    /**
     * Account creation timestamp.
     */
    private LocalDateTime createdAt;

    /**
     * Last update timestamp.
     */
    private LocalDateTime updatedAt;

    /**
     * Default constructor.
     */
    public UserResponse() {
    }

    /**
     * Constructor from User entity.
     *
     * @param user the user entity
     */
    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phoneNumber = user.getPhoneNumber();
        this.dateOfBirth = user.getDateOfBirth();
        this.address = user.getAddress();
        this.role = user.getRole();
        this.isActive = user.getIsActive();
        this.isAccountLocked = user.getIsAccountLocked();
        this.lastLoginDateTime = user.getLastLoginDateTime();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public User.Role getRole() {
        return role;
    }

    public void setRole(User.Role role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getIsAccountLocked() {
        return isAccountLocked;
    }

    public void setIsAccountLocked(Boolean isAccountLocked) {
        this.isAccountLocked = isAccountLocked;
    }

    public LocalDateTime getLastLoginDateTime() {
        return lastLoginDateTime;
    }

    public void setLastLoginDateTime(LocalDateTime lastLoginDateTime) {
        this.lastLoginDateTime = lastLoginDateTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Gets the full name of the user.
     *
     * @return the full name
     */
    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

    /**
     * Checks if the user is an admin.
     *
     * @return true if admin
     */
    public boolean isAdmin() {
        return role == User.Role.ADMIN;
    }

    /**
     * Checks if the user is a customer.
     *
     * @return true if customer
     */
    public boolean isCustomer() {
        return role == User.Role.CUSTOMER;
    }

    /**
     * Checks if the user account is active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return isActive != null && isActive;
    }

    /**
     * Checks if the user account is locked.
     *
     * @return true if locked
     */
    public boolean isAccountLocked() {
        return isAccountLocked != null && isAccountLocked;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                '}';
    }
}