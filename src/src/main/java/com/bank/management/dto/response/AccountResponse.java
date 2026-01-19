package com.bank.management.dto.response;

import com.bank.management.entity.Account;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for account response.
 *
 * This DTO provides account information for API responses,
 * including balance and account details.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
public class AccountResponse {

    /**
     * Account ID.
     */
    private Long id;

    /**
     * Account number.
     */
    private String accountNumber;

    /**
     * Account type.
     */
    private Account.AccountType accountType;

    /**
     * Current balance.
     */
    private BigDecimal balance;

    /**
     * Account currency.
     */
    private String currency;

    /**
     * Account status.
     */
    private Account.AccountStatus status;

    /**
     * Branch name.
     */
    private String branchName;

    /**
     * IFSC code.
     */
    private String ifscCode;

    /**
     * Account opening date.
     */
    private LocalDate openedDate;

    /**
     * Account closure date.
     */
    private LocalDate closedDate;

    /**
     * Account holder name.
     */
    private String accountHolderName;

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
    public AccountResponse() {
    }

    /**
     * Constructor from Account entity.
     *
     * @param account the account entity
     */
    public AccountResponse(Account account) {
        this.id = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.accountType = account.getAccountType();
        this.balance = account.getBalance();
        this.currency = account.getCurrency();
        this.status = account.getStatus();
        this.branchName = account.getBranchName();
        this.ifscCode = account.getIfscCode();
        this.openedDate = account.getOpenedDate();
        this.closedDate = account.getClosedDate();
        this.accountHolderName = account.getAccountHolderName();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Account.AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(Account.AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Account.AccountStatus getStatus() {
        return status;
    }

    public void setStatus(Account.AccountStatus status) {
        this.status = status;
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

    public LocalDate getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(LocalDate openedDate) {
        this.openedDate = openedDate;
    }

    public LocalDate getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDate closedDate) {
        this.closedDate = closedDate;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
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
     * Checks if the account is active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return status == Account.AccountStatus.ACTIVE;
    }

    /**
     * Checks if the account is blocked.
     *
     * @return true if blocked
     */
    public boolean isBlocked() {
        return status == Account.AccountStatus.BLOCKED;
    }

    /**
     * Checks if the account is closed.
     *
     * @return true if closed
     */
    public boolean isClosed() {
        return status == Account.AccountStatus.CLOSED;
    }

    /**
     * Gets the account status as a user-friendly string.
     *
     * @return the status string
     */
    public String getStatusDisplay() {
        return switch (status) {
            case ACTIVE -> "Active";
            case BLOCKED -> "Blocked";
            case CLOSED -> "Closed";
        };
    }

    /**
     * Gets the account type as a user-friendly string.
     *
     * @return the type string
     */
    public String getAccountTypeDisplay() {
        return switch (accountType) {
            case SAVINGS -> "Savings Account";
            case CURRENT -> "Current Account";
            case FIXED_DEPOSIT -> "Fixed Deposit";
        };
    }

    /**
     * Formats the balance with currency symbol.
     *
     * @return the formatted balance
     */
    public String getFormattedBalance() {
        return currency + " " + balance.toString();
    }

    @Override
    public String toString() {
        return "AccountResponse{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }
}