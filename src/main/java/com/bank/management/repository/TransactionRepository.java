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

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findByTransactionId(String transactionId);

    boolean existsByTransactionId(String transactionId);

    List<Transaction> findByAccount(Account account);

    Page<Transaction> findByAccount(Account account, Pageable pageable);

    List<Transaction> findByAccountId(Long accountId);

    Page<Transaction> findByAccountId(Long accountId, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE t.account.user = :user")
    Page<Transaction> findByUser(@Param("user") User user, Pageable pageable);

    List<Transaction> findByTransactionType(Transaction.TransactionType transactionType);

    List<Transaction> findByTransactionStatus(Transaction.TransactionStatus transactionStatus);

    List<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByAccountIdAndTransactionDateBetween(
            Long accountId, LocalDateTime startDate, LocalDateTime endDate);

    long countByAccount(Account account);

    long countByAccountAndTransactionType(
            Account account, Transaction.TransactionType transactionType);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.account.user = :user")
    long countByUser(@Param("user") User user);
}
