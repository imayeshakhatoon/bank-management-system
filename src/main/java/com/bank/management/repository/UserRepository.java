package com.bank.management.repository;

import com.bank.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity operations.
 *
 * Provides data access methods for user management with custom queries
 * for authentication, profile management, and administrative functions.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username.
     *
     * @param username the username
     * @return optional user
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email.
     *
     * @param email the email
     * @return optional user
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if username exists.
     *
     * @param username the username
     * @return true if exists
     */
    boolean existsByUsername(String username);

    /**
     * Check if email exists.
     *
     * @param email the email
     * @return true if exists
     */
    boolean existsByEmail(String email);

    /**
     * Check if username exists excluding specific user ID.
     *
     * @param username the username
     * @param userId the user ID to exclude
     * @return true if exists
     */
    boolean existsByUsernameAndIdNot(String username, Long userId);

    /**
     * Check if email exists excluding specific user ID.
     *
     * @param email the email
     * @param userId the user ID to exclude
     * @return true if exists
     */
    boolean existsByEmailAndIdNot(String email, Long userId);

    /**
     * Find all users by role.
     *
     * @param role the user role
     * @return list of users
     */
    List<User> findByRole(User.Role role);

    /**
     * Find all active users by role.
     *
     * @param role the user role
     * @param isActive the active status
     * @return list of users
     */
    List<User> findByRoleAndIsActive(User.Role role, Boolean isActive);

    /**
     * Find users by active status.
     *
     * @param isActive the active status
     * @return list of users
     */
    List<User> findByIsActive(Boolean isActive);

    /**
     * Find users by account locked status.
     *
     * @param isAccountLocked the locked status
     * @return list of users
     */
    List<User> findByIsAccountLocked(Boolean isAccountLocked);

    /**
     * Find users with failed login attempts exceeding threshold.
     *
     * @param threshold the failed attempts threshold
     * @return list of users
     */
    @Query("SELECT u FROM User u WHERE u.failedLoginAttempts >= :threshold")
    List<User> findUsersWithExcessiveFailedAttempts(@Param("threshold") Integer threshold);

    /**
     * Update user last login timestamp.
     *
     * @param userId the user ID
     * @param lastLogin the last login timestamp
     */
    @Modifying
    @Query("UPDATE User u SET u.lastLoginDateTime = :lastLogin WHERE u.id = :userId")
    void updateLastLogin(@Param("userId") Long userId, @Param("lastLogin") LocalDateTime lastLogin);

    /**
     * Increment failed login attempts for user.
     *
     * @param userId the user ID
     */
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = u.failedLoginAttempts + 1 WHERE u.id = :userId")
    void incrementFailedLoginAttempts(@Param("userId") Long userId);

    /**
     * Reset failed login attempts for user.
     *
     * @param userId the user ID
     */
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = 0 WHERE u.id = :userId")
    void resetFailedLoginAttempts(@Param("userId") Long userId);

    /**
     * Lock user account.
     *
     * @param userId the user ID
     */
    @Modifying
    @Query("UPDATE User u SET u.isAccountLocked = true WHERE u.id = :userId")
    void lockUserAccount(@Param("userId") Long userId);

    /**
     * Unlock user account.
     *
     * @param userId the user ID
     */
    @Modifying
    @Query("UPDATE User u SET u.isAccountLocked = false WHERE u.id = :userId")
    void unlockUserAccount(@Param("userId") Long userId);

    /**
     * Activate user account.
     *
     * @param userId the user ID
     */
    @Modifying
    @Query("UPDATE User u SET u.isActive = true WHERE u.id = :userId")
    void activateUser(@Param("userId") Long userId);

    /**
     * Deactivate user account.
     *
     * @param userId the user ID
     */
    @Modifying
    @Query("UPDATE User u SET u.isActive = false WHERE u.id = :userId")
    void deactivateUser(@Param("userId") Long userId);

    /**
     * Count users by role.
     *
     * @param role the user role
     * @return the count
     */
    long countByRole(User.Role role);

    /**
     * Count active users by role.
     *
     * @param role the user role
     * @param isActive the active status
     * @return the count
     */
    long countByRoleAndIsActive(User.Role role, Boolean isActive);

    /**
     * Find users created within date range.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return list of users
     */
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    List<User> findUsersCreatedBetween(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    /**
     * Find users who haven't logged in since specified date.
     *
     * @param since the date since when they haven't logged in
     * @return list of users
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginDateTime IS NULL OR u.lastLoginDateTime < :since")
    List<User> findInactiveUsers(@Param("since") LocalDateTime since);

    /**
     * Search users by name or email.
     *
     * @param searchTerm the search term
     * @return list of users
     */
    @Query("SELECT u FROM User u WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<User> searchUsers(@Param("searchTerm") String searchTerm);
}