package com.bank.management.dto.request;

import jakarta.validation.constraints.*;
import com.bank.management.entity.User;

import java.time.LocalDate;

/**
 * DTO for user profile update requests.
 *
 * This DTO handles profile updates with selective field validation
 * and business rules for profile management.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
public class UpdateProfileRequest {

    /**
     * First name of the user.
     */
    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "First name can only contain letters and spaces")
    private String firstName;

    /**
     * Last name of the user.
     */
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Last name can only contain letters and spaces")
    private String lastName;

    /**
     * Phone number of the user.
     */
    @Size(max = 15, message = "Phone number must not exceed 15 characters")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Phone number format is invalid")
    private String phoneNumber;

    /**
     * Date of birth of the user.
     */
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    /**
     * Address of the user.
     */
    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    /**
     * Current password for verification (required for sensitive updates).
     */
    @Size(min = 8, max = 255, message = "Current password must be between 8 and 255 characters")
    private String currentPassword;

    /**
     * New password (optional).
     */
    @Size(min = 8, max = 255, message = "New password must be between 8 and 255 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$",
             message = "New password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String newPassword;

    /**
     * Confirmation of the new password.
     */
    private String confirmNewPassword;

    /**
     * Default constructor.
     */
    public UpdateProfileRequest() {
    }

    // Getters and Setters

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

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    /**
     * Checks if any basic profile fields are being updated.
     *
     * @return true if profile fields are being updated
     */
    public boolean hasProfileUpdates() {
        return (firstName != null && !firstName.trim().isEmpty()) ||
               (lastName != null && !lastName.trim().isEmpty()) ||
               (phoneNumber != null && !phoneNumber.trim().isEmpty()) ||
               (dateOfBirth != null) ||
               (address != null && !address.trim().isEmpty());
    }

    /**
     * Checks if password is being updated.
     *
     * @return true if password update is requested
     */
    public boolean hasPasswordUpdate() {
        return newPassword != null && !newPassword.trim().isEmpty();
    }

    /**
     * Validates password update requirements.
     *
     * @return true if password update is valid
     */
    public boolean isPasswordUpdateValid() {
        return hasPasswordUpdate() &&
               currentPassword != null && !currentPassword.trim().isEmpty() &&
               confirmNewPassword != null && newPassword.equals(confirmNewPassword);
    }

    /**
     * Gets the full name from updated fields.
     *
     * @return the full name
     */
    public String getFullName() {
        String fName = (firstName != null && !firstName.trim().isEmpty()) ? firstName : "";
        String lName = (lastName != null && !lastName.trim().isEmpty()) ? lastName : "";
        return (fName + " " + lName).trim();
    }

    /**
     * Applies the updates to the given user entity.
     *
     * @param user the user to update
     */
    public void applyUpdates(User user) {
        if (firstName != null && !firstName.trim().isEmpty()) {
            user.setFirstName(firstName);
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            user.setLastName(lastName);
        }
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            user.setPhoneNumber(phoneNumber);
        }
        if (dateOfBirth != null) {
            user.setDateOfBirth(dateOfBirth);
        }
        if (address != null && !address.trim().isEmpty()) {
            user.setAddress(address);
        }
    }

    @Override
    public String toString() {
        return "UpdateProfileRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", hasPasswordUpdate=" + hasPasswordUpdate() +
                '}';
    }
}