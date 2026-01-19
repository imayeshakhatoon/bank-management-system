package com.bank.management.repository;

import com.bank.management.entity.User;
import com.bank.management.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for UserSession entity operations.
 *
 * Provides data access methods for session management, security auditing,
 * and concurrent session tracking.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    /**
     * Find session by session token.
     *
     * @param sessionToken the session token
     * @return optional session
     */
    Optional<UserSession> findBySessionToken(String sessionToken);

    /**
     * Find active session by session token.
     *
     * @param sessionToken the session token
     * @return optional session
     */
    @Query("SELECT s FROM UserSession s WHERE s.sessionToken = :sessionToken AND s.isActive = true")
    Optional<UserSession> findActiveBySessionToken(@Param("sessionToken") String sessionToken);

    /**
     * Find sessions by user.
     *
     * @param user the user
     * @return list of sessions
     */
    List<UserSession> findByUser(User user);

    /**
     * Find active sessions by user.
     *
     * @param user the user
     * @return list of active sessions
     */
    @Query("SELECT s FROM UserSession s WHERE s.user = :user AND s.isActive = true")
    List<UserSession> findActiveByUser(@Param("user") User user);

    /**
     * Find sessions by user ID.
     *
     * @param userId the user ID
     * @return list of sessions
     */
    List<UserSession> findByUserId(Long userId);

    /**
     * Find active sessions by user ID.
     *
     * @param userId the user ID
     * @return list of active sessions
     */
    @Query("SELECT s FROM UserSession s WHERE s.user.id = :userId AND s.isActive = true")
    List<UserSession> findActiveByUserId(@Param("userId") Long userId);

    /**
     * Find all active sessions.
     *
     * @return list of active sessions
     */
    List<UserSession> findByIsActiveTrue();

    /**
     * Find expired sessions.
     *
     * @param currentTime the current time
     * @param timeoutMinutes the timeout in minutes
     * @return list of expired sessions
     */
    @Query("SELECT s FROM UserSession s WHERE s.isActive = true AND s.loginDateTime < :expiryTime")
    List<UserSession> findExpiredSessions(@Param("expiryTime") LocalDateTime expiryTime);

    /**
     * Count active sessions by user.
     *
     * @param user the user
     * @return the count
     */
    @Query("SELECT COUNT(s) FROM UserSession s WHERE s.user = :user AND s.isActive = true")
    long countActiveByUser(@Param("user") User user);

    /**
     * Count total active sessions.
     *
     * @return the count
     */
    long countByIsActiveTrue();

    /**
     * Deactivate session by token.
     *
     * @param sessionToken the session token
     * @param logoutTime the logout time
     */
    @Modifying
    @Query("UPDATE UserSession s SET s.isActive = false, s.logoutDateTime = :logoutTime WHERE s.sessionToken = :sessionToken")
    void deactivateBySessionToken(@Param("sessionToken") String sessionToken, @Param("logoutTime") LocalDateTime logoutTime);

    /**
     * Deactivate all sessions for user.
     *
     * @param userId the user ID
     * @param logoutTime the logout time
     */
    @Modifying
    @Query("UPDATE UserSession s SET s.isActive = false, s.logoutDateTime = :logoutTime WHERE s.user.id = :userId AND s.isActive = true")
    void deactivateAllByUserId(@Param("userId") Long userId, @Param("logoutTime") LocalDateTime logoutTime);

    /**
     * Deactivate expired sessions.
     *
     * @param expiryTime the expiry time
     * @param logoutTime the logout time
     * @return number of sessions deactivated
     */
    @Modifying
    @Query("UPDATE UserSession s SET s.isActive = false, s.logoutDateTime = :logoutTime WHERE s.isActive = true AND s.loginDateTime < :expiryTime")
    int deactivateExpiredSessions(@Param("expiryTime") LocalDateTime expiryTime, @Param("logoutTime") LocalDateTime logoutTime);

    /**
     * Find sessions by IP address.
     *
     * @param ipAddress the IP address
     * @return list of sessions
     */
    List<UserSession> findByIpAddress(String ipAddress);

    /**
     * Find active sessions by IP address.
     *
     * @param ipAddress the IP address
     * @return list of active sessions
     */
    @Query("SELECT s FROM UserSession s WHERE s.ipAddress = :ipAddress AND s.isActive = true")
    List<UserSession> findActiveByIpAddress(@Param("ipAddress") String ipAddress);

    /**
     * Find sessions within date range.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return list of sessions
     */
    List<UserSession> findByLoginDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get session duration statistics.
     *
     * @return average session duration in minutes
     */
    @Query("SELECT AVG(TIMESTAMPDIFF(MINUTE, s.loginDateTime, COALESCE(s.logoutDateTime, CURRENT_TIMESTAMP))) " +
           "FROM UserSession s WHERE s.logoutDateTime IS NOT NULL")
    Double getAverageSessionDuration();

    /**
     * Get login attempts by date.
     *
     * @param date the date
     * @return number of login attempts
     */
    @Query("SELECT COUNT(s) FROM UserSession s WHERE DATE(s.loginDateTime) = :date")
    long countLoginsByDate(@Param("date") LocalDateTime date);

    /**
     * Get failed login statistics.
     *
     * @return array with [total_sessions, active_sessions, expired_sessions]
     */
    @Query("SELECT COUNT(s), " +
           "SUM(CASE WHEN s.isActive = true THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN s.isActive = false AND s.logoutDateTime IS NOT NULL THEN 1 ELSE 0 END) " +
           "FROM UserSession s")
    Object[] getSessionStatistics();

    /**
     * Find sessions by user agent pattern.
     *
     * @param userAgentPattern the user agent pattern
     * @return list of sessions
     */
    @Query("SELECT s FROM UserSession s WHERE LOWER(s.userAgent) LIKE LOWER(CONCAT('%', :userAgentPattern, '%'))")
    List<UserSession> findByUserAgentPattern(@Param("userAgentPattern") String userAgentPattern);

    /**
     * Clean up old inactive sessions.
     *
     * @param cutoffDate the cutoff date
     * @return number of sessions deleted
     */
    @Modifying
    @Query("DELETE FROM UserSession s WHERE s.isActive = false AND s.logoutDateTime < :cutoffDate")
    int deleteOldInactiveSessions(@Param("cutoffDate") LocalDateTime cutoffDate);

    /**
     * Get concurrent sessions per user.
     *
     * @return list of arrays [user_id, username, active_sessions_count]
     */
    @Query("SELECT u.id, u.username, COUNT(s) FROM User u " +
           "LEFT JOIN UserSession s ON u.id = s.user.id AND s.isActive = true " +
           "GROUP BY u.id, u.username HAVING COUNT(s) > 0")
    List<Object[]> getConcurrentSessionsPerUser();
}