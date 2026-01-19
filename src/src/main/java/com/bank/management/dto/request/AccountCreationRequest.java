package com.bank.management.dto.request;

import jakarta.validation.constraints.*;
import com.bank.management.entity.Account;

import java.math.BigDecimal;

/**
 * DTO for account creation requests.
 *
 * This DTO handles new bank account creation with validation
 * for account type, initial deposit, and business rules.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
public class AccountCreationRequest {

    /**
     * Type of account to create.
     */
    @NotNull(message = "Account type is required")
    private Account.AccountType accountType = Account.AccountType.SAVINGS;

    /**
     * Initial deposit amount.
     */
    @NotNull(message = "Initial deposit is required")
    @DecimalMin(value = "1000.00", message = "Initial deposit must be at least 1000.00")
    @Digits(integer = 15, fraction = 2, message = "Initial deposit must have at most 15 integer digits and 2 decimal places")
    private BigDecimal initialDeposit;

    /**
     * Branch name where the account should be opened.
     */
    @Size(max = 100, message = "Branch name must not exceed 100 characters")
    private String branchName;

    /**
     * IFSC code of the branch.
     */
    @Size(max = 11, message = "IFSC code must not exceed 11 characters")
    @Pattern(regexp = "^[A-Z]{4}\\d{7}$", message = "IFSC code must be 4 uppercase letters followed by 7 digits")
    private String ifscCode;

    /**
     * Currency for the account.
     */
    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be uppercase letters only")
    private String currency = "INR";

    /**
     * Default constructor.
     */
    public AccountCreationRequest() {
    }

    /**
     * Constructor with essential fields.
     *
     * @param accountType the account type
     * @param initialDeposit the initial deposit amount
     */
    public AccountCreationRequest(Account.AccountType accountType, BigDecimal initialDeposit) {
        this.accountType = accountType;
        this.initialDeposit = initialDeposit;
    }

    // Getters and Setters

    public Account.AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(Account.AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getInitialDeposit() {
        return initialDeposit;
    }

    public void setInitialDeposit(BigDecimal initialDeposit) {
        this.initialDeposit = initialDeposit;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Validates business rules for account creation.
     *
     * @return true if valid
     */
    public boolean isValid() {
        // Minimum balance requirements based on account type
        BigDecimal minBalance = switch (accountType) {
            case SAVINGS -> new BigDecimal("1000.00");
            case CURRENT -> new BigDecimal("5000.00");
            case FIXED_DEPOSIT -> new BigDecimal("10000.00");
        };

        return initialDeposit.compareTo(minBalance) >= 0;
    }

    /**
     * Gets the minimum balance required for the account type.
     *
     * @return the minimum balance
     */
    public BigDecimal getMinimumBalance() {
        return switch (accountType) {
            case SAVINGS -> new BigDecimal("1000.00");
            case CURRENT -> new BigDecimal("5000.00");
            case FIXED_DEPOSIT -> new BigDecimal("10000.00");
        };
    }

    @Override
    public String toString() {
        return "AccountCreationRequest{" +
                "accountType=" + accountType +
                ", initialDeposit=" + initialDeposit +
                ", currency='" + currency + '\'' +
                ", branchName='" + branchName + '\'' +
                '}';
    }
}