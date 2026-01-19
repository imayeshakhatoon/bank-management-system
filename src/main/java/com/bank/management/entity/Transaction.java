package com.bank.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Transaction entity representing financial transactions in the banking system.
 *
 * This entity handles deposits, withdrawals, and transfers with comprehensive
 * audit trails and business rule validation.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
@Entity
@Table(name = "transactions", indexes = {
    @Index(name = "idx_transaction_id", columnList = "transaction_id", unique = true),
    @Index(name = "idx_transaction_account_id", columnList = "account_id"),
    @Index(name = "idx_transaction_type", columnList = "transaction_type"),
    @Index(name = "idx_transaction_date", columnList = "transaction_date"),
    @Index(name = "idx_transaction_status", columnList = "transaction_status"),
    @Index(name = "idx_transaction_transfer_ref", columnList = "transfer_reference"),
    @Index(name = "idx_transaction_created_by", columnList = "created_by")
})
public class Transaction {

    /**
     * Unique identifier for the transaction.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Unique transaction ID for external reference (UUID).
     */
    @NotBlank(message = "Transaction ID is required")
    @Column(name = "transaction_id", nullable = false, unique = true, length = 36)
    private String transactionId;

    /**
     * The account associated with this transaction.
     */
    @NotNull(message = "Account is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    /**
     * Type of the transaction.
     */
    @NotNull(message = "Transaction type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 15)
    private TransactionType transactionType;

    /**
     * Amount involved in the transaction.
     */
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    @Digits(integer = 15, fraction = 2, message = "Amount must have at most 15 integer digits and 2 decimal places")
    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    /**
     * Balance before the transaction.
     */
    @NotNull(message = "Balance before is required")
    @DecimalMin(value = "0.00", message = "Balance before cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Balance before must have at most 15 integer digits and 2 decimal places")
    @Column(name = "balance_before", nullable = false, precision = 15, scale = 2)
    private BigDecimal balanceBefore;

    /**
     * Balance after the transaction.
     */
    @NotNull(message = "Balance after is required")
    @DecimalMin(value = "0.00", message = "Balance after cannot be negative")
    @Digits(integer = 15, fraction = 2, message = "Balance after must have at most 15 integer digits and 2 decimal places")
    @Column(name = "balance_after", nullable = false, precision = 15, scale = 2)
    private BigDecimal balanceAfter;

    /**
     * Description of the transaction.
     */
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Date and time when the transaction occurred.
     */
    @NotNull(message = "Transaction date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    /**
     * Reference account for transfers (source/destination account).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reference_account_id")
    private Account referenceAccount;

    /**
     * Transfer reference to link debit and credit transactions.
     */
    @Column(name = "transfer_reference", length = 36)
    private String transferReference;

    /**
     * Status of the transaction.
     */
    @NotNull(message = "Transaction status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", nullable = false, length = 15)
    private TransactionStatus transactionStatus = TransactionStatus.COMPLETED;

    /**
     * Reason for transaction failure (if applicable).
     */
    @Size(max = 500, message = "Failure reason must not exceed 500 characters")
    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    /**
     * User who initiated the transaction.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    /**
     * Timestamp when the transaction was created.
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Default constructor.
     */
    public Transaction() {
    }

    /**
     * Constructor for basic transaction.
     *
     * @param account the account
     * @param transactionType the transaction type
     * @param amount the amount
     * @param balanceBefore the balance before
     * @param balanceAfter the balance after
     * @param description the description
     */
    public Transaction(Account account, TransactionType transactionType, BigDecimal amount,
                      BigDecimal balanceBefore, BigDecimal balanceAfter, String description) {
        this.transactionId = UUID.randomUUID().toString();
        this.account = account;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.description = description;
    }

    /**
     * Constructor for transfer transactions.
     *
     * @param account the account
     * @param transactionType the transaction type
     * @param amount the amount
     * @param balanceBefore the balance before
     * @param balanceAfter the balance after
     * @param description the description
     * @param referenceAccount the reference account
     * @param transferReference the transfer reference
     */
    public Transaction(Account account, TransactionType transactionType, BigDecimal amount,
                      BigDecimal balanceBefore, BigDecimal balanceAfter, String description,
                      Account referenceAccount, String transferReference) {
        this(account, transactionType, amount, balanceBefore, balanceAfter, description);
        this.referenceAccount = referenceAccount;
        this.transferReference = transferReference;
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

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    public Account getReferenceAccount() {
        return referenceAccount;
    }

    public void setReferenceAccount(Account referenceAccount) {
        this.referenceAccount = referenceAccount;
    }

    public String getTransferReference() {
        return transferReference;
    }

    public void setTransferReference(String transferReference) {
        this.transferReference = transferReference;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Checks if the transaction is a credit transaction.
     *
     * @return true if credit transaction
     */
    public boolean isCredit() {
        return transactionType == TransactionType.DEPOSIT || transactionType == TransactionType.TRANSFER_IN;
    }

    /**
     * Checks if the transaction is a debit transaction.
     *
     * @return true if debit transaction
     */
    public boolean isDebit() {
        return transactionType == TransactionType.WITHDRAWAL || transactionType == TransactionType.TRANSFER_OUT;
    }

    /**
     * Checks if the transaction is completed.
     *
     * @return true if completed
     */
    public boolean isCompleted() {
        return transactionStatus == TransactionStatus.COMPLETED;
    }

    /**
     * Checks if the transaction is failed.
     *
     * @return true if failed
     */
    public boolean isFailed() {
        return transactionStatus == TransactionStatus.FAILED;
    }

    /**
     * Checks if the transaction is pending.
     *
     * @return true if pending
     */
    public boolean isPending() {
        return transactionStatus == TransactionStatus.PENDING;
    }

    /**
     * Gets the account number for display purposes.
     *
     * @return the account number
     */
    public String getAccountNumber() {
        return account != null ? account.getAccountNumber() : null;
    }

    /**
     * Gets the reference account number for transfers.
     *
     * @return the reference account number
     */
    public String getReferenceAccountNumber() {
        return referenceAccount != null ? referenceAccount.getAccountNumber() : null;
    }

    /**
     * Transaction type enumeration.
     */
    public enum TransactionType {
        DEPOSIT,
        WITHDRAWAL,
        TRANSFER_IN,
        TRANSFER_OUT
    }

    /**
     * Transaction status enumeration.
     */
    public enum TransactionStatus {
        PENDING,
        COMPLETED,
        FAILED,
        CANCELLED
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", transactionId='" + transactionId + '\'' +
                ", transactionType=" + transactionType +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                ", transactionStatus=" + transactionStatus +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction that = (Transaction) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}