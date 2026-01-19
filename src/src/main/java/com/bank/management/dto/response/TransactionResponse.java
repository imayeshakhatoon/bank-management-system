package com.bank.management.dto.response;

import com.bank.management.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for transaction response.
 *
 * This DTO provides transaction information for API responses,
 * including transaction details and status.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
public class TransactionResponse {

    /**
     * Transaction ID.
     */
    private Long id;

    /**
     * Unique transaction identifier.
     */
    private String transactionId;

    /**
     * Account number.
     */
    private String accountNumber;

    /**
     * Transaction type.
     */
    private Transaction.TransactionType transactionType;

    /**
     * Transaction amount.
     */
    private BigDecimal amount;

    /**
     * Balance before transaction.
     */
    private BigDecimal balanceBefore;

    /**
     * Balance after transaction.
     */
    private BigDecimal balanceAfter;

    /**
     * Transaction description.
     */
    private String description;

    /**
     * Transaction date and time.
     */
    private LocalDateTime transactionDate;

    /**
     * Reference account number (for transfers).
     */
    private String referenceAccountNumber;

    /**
     * Transfer reference ID.
     */
    private String transferReference;

    /**
     * Transaction status.
     */
    private Transaction.TransactionStatus transactionStatus;

    /**
     * Failure reason (if applicable).
     */
    private String failureReason;

    /**
     * Transaction creation timestamp.
     */
    private LocalDateTime createdAt;

    /**
     * Default constructor.
     */
    public TransactionResponse() {
    }

    /**
     * Constructor from Transaction entity.
     *
     * @param transaction the transaction entity
     */
    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.transactionId = transaction.getTransactionId();
        this.accountNumber = transaction.getAccountNumber();
        this.transactionType = transaction.getTransactionType();
        this.amount = transaction.getAmount();
        this.balanceBefore = transaction.getBalanceBefore();
        this.balanceAfter = transaction.getBalanceAfter();
        this.description = transaction.getDescription();
        this.transactionDate = transaction.getTransactionDate();
        this.referenceAccountNumber = transaction.getReferenceAccountNumber();
        this.transferReference = transaction.getTransferReference();
        this.transactionStatus = transaction.getTransactionStatus();
        this.failureReason = transaction.getFailureReason();
        this.createdAt = transaction.getCreatedAt();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Transaction.TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Transaction.TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalanceBefore() {
        return balanceBefore;
    }

    public void setBalanceBefore(BigDecimal balanceBefore) {
        this.balanceBefore = balanceBefore;
    }

    public BigDecimal getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(BigDecimal balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getReferenceAccountNumber() {
        return referenceAccountNumber;
    }

    public void setReferenceAccountNumber(String referenceAccountNumber) {
        this.referenceAccountNumber = referenceAccountNumber;
    }

    public String getTransferReference() {
        return transferReference;
    }

    public void setTransferReference(String transferReference) {
        this.transferReference = transferReference;
    }

    public Transaction.TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(Transaction.TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Checks if this is a credit transaction.
     *
     * @return true if credit
     */
    public boolean isCredit() {
        return transactionType == Transaction.TransactionType.DEPOSIT ||
               transactionType == Transaction.TransactionType.TRANSFER_IN;
    }

    /**
     * Checks if this is a debit transaction.
     *
     * @return true if debit
     */
    public boolean isDebit() {
        return transactionType == Transaction.TransactionType.WITHDRAWAL ||
               transactionType == Transaction.TransactionType.TRANSFER_OUT;
    }

    /**
     * Checks if the transaction is completed.
     *
     * @return true if completed
     */
    public boolean isCompleted() {
        return transactionStatus == Transaction.TransactionStatus.COMPLETED;
    }

    /**
     * Checks if the transaction is failed.
     *
     * @return true if failed
     */
    public boolean isFailed() {
        return transactionStatus == Transaction.TransactionStatus.FAILED;
    }

    /**
     * Checks if the transaction is pending.
     *
     * @return true if pending
     */
    public boolean isPending() {
        return transactionStatus == Transaction.TransactionStatus.PENDING;
    }

    /**
     * Gets the transaction type as a user-friendly string.
     *
     * @return the type string
     */
    public String getTransactionTypeDisplay() {
        return switch (transactionType) {
            case DEPOSIT -> "Deposit";
            case WITHDRAWAL -> "Withdrawal";
            case TRANSFER_IN -> "Transfer In";
            case TRANSFER_OUT -> "Transfer Out";
        };
    }

    /**
     * Gets the transaction status as a user-friendly string.
     *
     * @return the status string
     */
    public String getStatusDisplay() {
        return switch (transactionStatus) {
            case COMPLETED -> "Completed";
            case PENDING -> "Pending";
            case FAILED -> "Failed";
            case CANCELLED -> "Cancelled";
        };
    }

    /**
     * Gets the net effect on balance.
     *
     * @return positive for credit, negative for debit
     */
    public BigDecimal getNetAmount() {
        return isCredit() ? amount : amount.negate();
    }

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "transactionId='" + transactionId + '\'' +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", transactionStatus=" + transactionStatus +
                '}';
    }
}