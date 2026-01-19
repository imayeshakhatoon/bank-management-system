package com.bank.management.dto.request;

import jakarta.validation.constraints.*;
import com.bank.management.entity.Transaction;

import java.math.BigDecimal;

/**
 * DTO for transaction requests.
 *
 * This DTO handles various transaction types (deposit, withdrawal, transfer)
 * with proper validation and business rules.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
public class TransactionRequest {

    /**
     * Account number for the transaction.
     */
    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^A\\d{9}$", message = "Account number must start with 'A' followed by 9 digits")
    private String accountNumber;

    /**
     * Type of transaction.
     */
    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;

    /**
     * Amount for the transaction.
     */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.00", message = "Amount must be at least 1.00")
    @Digits(integer = 15, fraction = 2, message = "Amount must have at most 15 integer digits and 2 decimal places")
    private BigDecimal amount;

    /**
     * Description of the transaction.
     */
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    /**
     * Destination account number for transfers.
     */
    @Pattern(regexp = "^A\\d{9}$", message = "Destination account number must start with 'A' followed by 9 digits")
    private String toAccountNumber;

    /**
     * Transaction type enumeration.
     */
    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER
    }

    /**
     * Default constructor.
     */
    public TransactionRequest() {
    }

    /**
     * Constructor for deposit/withdrawal.
     *
     * @param accountNumber the account number
     * @param transactionType the transaction type
     * @param amount the amount
     * @param description the description
     */
    public TransactionRequest(String accountNumber, TransactionType transactionType,
                            BigDecimal amount, String description) {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
    }

    /**
     * Constructor for transfer.
     *
     * @param accountNumber the source account number
     * @param amount the amount
     * @param toAccountNumber the destination account number
     * @param description the description
     */
    public TransactionRequest(String accountNumber, BigDecimal amount,
                            String toAccountNumber, String description) {
        this.accountNumber = accountNumber;
        this.transactionType = TransactionType.TRANSFER;
        this.amount = amount;
        this.toAccountNumber = toAccountNumber;
        this.description = description;
    }

    // Getters and Setters

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    /**
     * Validates that the transaction is properly configured.
     *
     * @return true if valid
     */
    public boolean isValid() {
        // For transfers, destination account is required
        if (transactionType == TransactionType.TRANSFER) {
            return toAccountNumber != null && !toAccountNumber.trim().isEmpty()
                   && !accountNumber.equals(toAccountNumber);
        }

        // For deposit/withdrawal, no destination account needed
        return transactionType == TransactionType.DEPOSIT || transactionType == TransactionType.WITHDRAWAL;
    }

    /**
     * Checks if this is a transfer transaction.
     *
     * @return true if transfer
     */
    public boolean isTransfer() {
        return transactionType == TransactionType.TRANSFER;
    }

    /**
     * Checks if this is a deposit transaction.
     *
     * @return true if deposit
     */
    public boolean isDeposit() {
        return transactionType == TransactionType.DEPOSIT;
    }

    /**
     * Checks if this is a withdrawal transaction.
     *
     * @return true if withdrawal
     */
    public boolean isWithdrawal() {
        return transactionType == TransactionType.WITHDRAWAL;
    }

    /**
     * Gets the transaction description with default values.
     *
     * @return the description
     */
    public String getTransactionDescription() {
        if (description != null && !description.trim().isEmpty()) {
            return description;
        }

        return switch (transactionType) {
            case DEPOSIT -> "Cash Deposit";
            case WITHDRAWAL -> "Cash Withdrawal";
            case TRANSFER -> "Fund Transfer to " + toAccountNumber;
        };
    }

    /**
     * Converts this DTO transaction type to entity transaction type.
     *
     * @return the entity transaction type
     */
    public Transaction.TransactionType toEntityTransactionType() {
        return switch (transactionType) {
            case DEPOSIT -> Transaction.TransactionType.DEPOSIT;
            case WITHDRAWAL -> Transaction.TransactionType.WITHDRAWAL;
            case TRANSFER -> Transaction.TransactionType.TRANSFER_OUT;
        };
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "accountNumber='" + accountNumber + '\'' +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", toAccountNumber='" + toAccountNumber + '\'' +
                '}';
    }
}