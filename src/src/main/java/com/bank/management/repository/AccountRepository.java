package com.bank.management.repository;

import com.bank.management.entity.Account;
import com.bank.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Account entity operations.
 *
 * Provides data access methods for account management with custom queries
 * for balance operations, account status management, and reporting.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find account by account number.
     *
     * @param accountNumber the account number
     * @return optional account
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Check if account number exists.
     *
     * @param accountNumber the account number
     * @return true if exists
     */
    boolean existsByAccountNumber(String accountNumber);

    /**
     * Find accounts by user.
     *
     * @param user the user
     * @return list of accounts
     */
    List<Account> findByUser(User user);

    /**
     * Find accounts by user ID.
     *
     * @param userId the user ID
     * @return list of accounts
     */
    List<Account> findByUserId(Long userId);

    /**
     * Find accounts by user and status.
     *
     * @param user the user
     * @param status the account status
     * @return list of accounts
     */
    List<Account> findByUserAndStatus(User user, Account.AccountStatus status);

    /**
     * Find accounts by status.
     *
     * @param status the account status
     * @return list of accounts
     */
    List<Account> findByStatus(Account.AccountStatus status);

    /**
     * Find accounts by type.
     *
     * @param accountType the account type
     * @return list of accounts
     */
    List<Account> findByAccountType(Account.AccountType accountType);

    /**
     * Find accounts by user and type.
     *
     * @param user the user
     * @param accountType the account type
     * @return list of accounts
     */
    List<Account> findByUserAndAccountType(User user, Account.AccountType accountType);

    /**
     * Find accounts opened within date range.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return list of accounts
     */
    List<Account> findByOpenedDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Count accounts by user.
     *
     * @param user the user
     * @return the count
     */
    long countByUser(User user);

    /**
     * Count accounts by user and status.
     *
     * @param user the user
     * @param status the account status
     * @return the count
     */
    long countByUserAndStatus(User user, Account.AccountStatus status);

    /**
     * Count accounts by status.
     *
     * @param status the account status
     * @return the count
     */
    long countByStatus(Account.AccountStatus status);

    /**
     * Get total balance for user across all accounts.
     *
     * @param userId the user ID
     * @return the total balance
     */
    @Query("SELECT COALESCE(SUM(a.balance), 0) FROM Account a WHERE a.user.id = :userId AND a.status = 'ACTIVE'")
    BigDecimal getTotalBalanceByUserId(@Param("userId") Long userId);

    /**
     * Get total balance across all accounts.
     *
     * @return the total balance
     */
    @Query("SELECT COALESCE(SUM(a.balance), 0) FROM Account a WHERE a.status = 'ACTIVE'")
    BigDecimal getTotalBalanceAllAccounts();

    /**
     * Update account balance.
     *
     * @param accountId the account ID
     * @param newBalance the new balance
     */
    @Modifying
    @Query("UPDATE Account a SET a.balance = :newBalance WHERE a.id = :accountId")
    void updateBalance(@Param("accountId") Long accountId, @Param("newBalance") BigDecimal newBalance);

    /**
     * Credit amount to account balance.
     *
     * @param accountId the account ID
     * @param amount the amount to credit
     */
    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.id = :accountId")
    void creditBalance(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);

    /**
     * Debit amount from account balance.
     *
     * @param accountId the account ID
     * @param amount the amount to debit
     */
    @Modifying
    @Query("UPDATE Account a SET a.balance = a.balance - :amount WHERE a.id = :accountId")
    void debitBalance(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount);

    /**
     * Block account.
     *
     * @param accountId the account ID
     */
    @Modifying
    @Query("UPDATE Account a SET a.status = 'BLOCKED' WHERE a.id = :accountId")
    void blockAccount(@Param("accountId") Long accountId);

    /**
     * Unblock account.
     *
     * @param accountId the account ID
     */
    @Modifying
    @Query("UPDATE Account a SET a.status = 'ACTIVE' WHERE a.id = :accountId")
    void unblockAccount(@Param("accountId") Long accountId);

    /**
     * Close account.
     *
     * @param accountId the account ID
     * @param closedDate the closure date
     */
    @Modifying
    @Query("UPDATE Account a SET a.status = 'CLOSED', a.closedDate = :closedDate WHERE a.id = :accountId")
    void closeAccount(@Param("accountId") Long accountId, @Param("closedDate") LocalDate closedDate);

    /**
     * Find accounts with balance below threshold.
     *
     * @param threshold the balance threshold
     * @return list of accounts
     */
    @Query("SELECT a FROM Account a WHERE a.balance < :threshold AND a.status = 'ACTIVE'")
    List<Account> findAccountsWithLowBalance(@Param("threshold") BigDecimal threshold);

    /**
     * Find high-value accounts.
     *
     * @param threshold the balance threshold
     * @return list of accounts
     */
    @Query("SELECT a FROM Account a WHERE a.balance >= :threshold AND a.status = 'ACTIVE'")
    List<Account> findHighValueAccounts(@Param("threshold") BigDecimal threshold);

    /**
     * Find accounts by IFSC code.
     *
     * @param ifscCode the IFSC code
     * @return list of accounts
     */
    List<Account> findByIfscCode(String ifscCode);

    /**
     * Search accounts by account holder name.
     *
     * @param searchTerm the search term
     * @return list of accounts
     */
    @Query("SELECT a FROM Account a WHERE LOWER(a.user.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(a.user.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Account> searchByAccountHolderName(@Param("searchTerm") String searchTerm);

    /**
     * Get account summary statistics.
     *
     * @return array with [total_accounts, active_accounts, blocked_accounts, closed_accounts]
     */
    @Query("SELECT COUNT(a), " +
           "SUM(CASE WHEN a.status = 'ACTIVE' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN a.status = 'BLOCKED' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN a.status = 'CLOSED' THEN 1 ELSE 0 END) " +
           "FROM Account a")
    Object[] getAccountStatistics();

    /**
     * Get account type distribution.
     *
     * @return array with [savings_count, current_count, fd_count]
     */
    @Query("SELECT " +
           "SUM(CASE WHEN a.accountType = 'SAVINGS' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN a.accountType = 'CURRENT' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN a.accountType = 'FIXED_DEPOSIT' THEN 1 ELSE 0 END) " +
           "FROM Account a WHERE a.status = 'ACTIVE'")
    Object[] getAccountTypeDistribution();

    /**
     * Find dormant accounts (no transactions in specified days).
     *
     * @param days the number of days
     * @return list of accounts
     */
    @Query("SELECT a FROM Account a WHERE a.status = 'ACTIVE' AND a.id NOT IN " +
           "(SELECT DISTINCT t.account.id FROM Transaction t WHERE t.transactionDate >= DATE_SUB(CURRENT_DATE, INTERVAL :days DAY))")
    List<Account> findDormantAccounts(@Param("days") int days);
}