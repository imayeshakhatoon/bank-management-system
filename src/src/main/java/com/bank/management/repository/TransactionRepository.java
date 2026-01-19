package com.bank.management.repository;

import com.bank.management.entity.Account;
import com.bank.management.entity.Transaction;
import com.bank.management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for Transaction entity operations.
 *
 * Provides data access methods for transaction management with custom queries
 * for transaction history, reporting, and financial analysis.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Find transaction by transaction ID.
     *
     * @param transactionId the transaction ID
     * @return optional transaction
     */
    Transaction findByTransactionId(String transactionId);

    /**
     * Find transactions by account.
     *
     * @param account the account
     * @return list of transactions
     */
    List<Transaction> findByAccount(Account account);

    /**
     * Find transactions by account with pagination.
     *
     * @param account the account
     * @param pageable the pagination information
     * @return page of transactions
     */
    Page<Transaction> findByAccount(Account account, Pageable pageable);

    /**
     * Find transactions by account ID.
     *
     * @param accountId the account ID
     * @return list of transactions
     */
    List<Transaction> findByAccountId(Long accountId);

    /**
     * Find transactions by account ID with pagination.
     *
     * @param accountId the account ID
     * @param pageable the pagination information
     * @return page of transactions
     */
    Page<Transaction> findByAccountId(Long accountId, Pageable pageable);

    /**
     * Find transactions by user (across all accounts).
     *
     * @param user the user
     * @return list of transactions
     */
    @Query("SELECT t FROM Transaction t WHERE t.account.user = :user")
    List<Transaction> findByUser(@Param("user") User user);

    /**
     * Find transactions by user with pagination.
     *
     * @param user the user
     * @param pageable the pagination information
     * @return page of transactions
     */
    @Query("SELECT t FROM Transaction t WHERE t.account.user = :user")
    Page<Transaction> findByUser(@Param("user") User user, Pageable pageable);

    /**
     * Find transactions by type.
     *
     * @param transactionType the transaction type
     * @return list of transactions
     */
    List<Transaction> findByTransactionType(Transaction.TransactionType transactionType);

    /**
     * Find transactions by status.
     *
     * @param transactionStatus the transaction status
     * @return list of transactions
     */
    List<Transaction> findByTransactionStatus(Transaction.TransactionStatus transactionStatus);

    /**
     * Find transactions by date range.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return list of transactions
     */
    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find transactions by account and date range.
     *
     * @param accountId the account ID
     * @param startDate the start date
     * @param endDate the end date
     * @return list of transactions
     */
    List<Transaction> findByAccountIdAndTransactionDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find transactions by account and type.
     *
     * @param accountId the account ID
     * @param transactionType the transaction type
     * @return list of transactions
     */
    List<Transaction> findByAccountIdAndTransactionType(Long accountId, Transaction.TransactionType transactionType);

    /**
     * Find transactions by transfer reference.
     *
     * @param transferReference the transfer reference
     * @return list of transactions
     */
    List<Transaction> findByTransferReference(String transferReference);

    /**
     * Count transactions by account.
     *
     * @param account the account
     * @return the count
     */
    long countByAccount(Account account);

    /**
     * Count transactions by account and type.
     *
     * @param account the account
     * @param transactionType the transaction type
     * @return the count
     */
    long countByAccountAndTransactionType(Account account, Transaction.TransactionType transactionType);

    /**
     * Count transactions by user.
     *
     * @param user the user
     * @return the count
     */
    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.account.user = :user")
    long countByUser(@Param("user") User user);

    /**
     * Get total transaction amount by account and type.
     *
     * @param accountId the account ID
     * @param transactionType the transaction type
     * @return the total amount
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.account.id = :accountId AND t.transactionType = :transactionType")
    BigDecimal getTotalAmountByAccountAndType(@Param("accountId") Long accountId,
                                             @Param("transactionType") Transaction.TransactionType transactionType);

    /**
     * Get total credit amount for account.
     *
     * @param accountId the account ID
     * @return the total credit amount
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.account.id = :accountId AND t.transactionType IN ('DEPOSIT', 'TRANSFER_IN')")
    BigDecimal getTotalCreditAmount(@Param("accountId") Long accountId);

    /**
     * Get total debit amount for account.
     *
     * @param accountId the account ID
     * @return the total debit amount
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.account.id = :accountId AND t.transactionType IN ('WITHDRAWAL', 'TRANSFER_OUT')")
    BigDecimal getTotalDebitAmount(@Param("accountId") Long accountId);

    /**
     * Get recent transactions for account.
     *
     * @param accountId the account ID
     * @param limit the maximum number of transactions
     * @return list of transactions
     */
    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId ORDER BY t.transactionDate DESC")
    List<Transaction> findRecentTransactions(@Param("accountId") Long accountId, org.springframework.data.domain.PageRequest pageRequest);

    /**
     * Find failed transactions.
     *
     * @return list of failed transactions
     */
    List<Transaction> findByTransactionStatus(Transaction.TransactionStatus.FAILED);

    /**
     * Find pending transactions.
     *
     * @return list of pending transactions
     */
    List<Transaction> findByTransactionStatus(Transaction.TransactionStatus.PENDING);

    /**
     * Get transaction summary for account.
     *
     * @param accountId the account ID
     * @return array with [total_transactions, total_credits, total_debits, avg_transaction_amount]
     */
    @Query("SELECT " +
           "COUNT(t), " +
           "COALESCE(SUM(CASE WHEN t.transactionType IN ('DEPOSIT', 'TRANSFER_IN') THEN t.amount ELSE 0 END), 0), " +
           "COALESCE(SUM(CASE WHEN t.transactionType IN ('WITHDRAWAL', 'TRANSFER_OUT') THEN t.amount ELSE 0 END), 0), " +
           "COALESCE(AVG(t.amount), 0) " +
           "FROM Transaction t WHERE t.account.id = :accountId")
    Object[] getTransactionSummary(@Param("accountId") Long accountId);

    /**
     * Get monthly transaction summary.
     *
     * @param year the year
     * @param month the month
     * @return list of daily transaction summaries
     */
    @Query("SELECT DATE(t.transactionDate), COUNT(t), SUM(t.amount) " +
           "FROM Transaction t WHERE YEAR(t.transactionDate) = :year AND MONTH(t.transactionDate) = :month " +
           "GROUP BY DATE(t.transactionDate) ORDER BY DATE(t.transactionDate)")
    List<Object[]> getMonthlyTransactionSummary(@Param("year") int year, @Param("month") int month);

    /**
     * Find large transactions above threshold.
     *
     * @param threshold the amount threshold
     * @return list of transactions
     */
    @Query("SELECT t FROM Transaction t WHERE t.amount >= :threshold ORDER BY t.amount DESC")
    List<Transaction> findLargeTransactions(@Param("threshold") BigDecimal threshold);

    /**
     * Get transaction statistics.
     *
     * @return array with [total_transactions, completed_transactions, failed_transactions, pending_transactions]
     */
    @Query("SELECT COUNT(t), " +
           "SUM(CASE WHEN t.transactionStatus = 'COMPLETED' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN t.transactionStatus = 'FAILED' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN t.transactionStatus = 'PENDING' THEN 1 ELSE 0 END) " +
           "FROM Transaction t")
    Object[] getTransactionStatistics();

    /**
     * Find transactions by description keyword.
     *
     * @param keyword the search keyword
     * @return list of transactions
     */
    @Query("SELECT t FROM Transaction t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Transaction> searchByDescription(@Param("keyword") String keyword);

    /**
     * Get transactions by amount range.
     *
     * @param minAmount the minimum amount
     * @param maxAmount the maximum amount
     * @return list of transactions
     */
    List<Transaction> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
}