package com.bank.management.security;

import com.bank.management.entity.User;
import com.bank.management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementation of UserDetailsService for Spring Security.
 *
 * This service loads user details by username and handles authentication-related
 * operations like updating last login time and managing failed login attempts.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Load user by username for authentication.
     *
     * @param username the username
     * @return UserDetails object
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        log.debug("Loaded user details for username: {}", username);
        return user;
    }

    /**
     * Load user by email for authentication.
     *
     * @param email the email
     * @return UserDetails object
     * @throws UsernameNotFoundException if user not found
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        log.debug("Loaded user details for email: {}", email);
        return user;
    }

    /**
     * Handle successful authentication.
     *
     * @param username the username
     */
    @Transactional
    public void handleSuccessfulAuthentication(String username) {
        try {
            userRepository.findByUsername(username).ifPresent(user -> {
                // Reset failed login attempts
                user.resetFailedLoginAttempts();

                // Update last login time
                user.setLastLoginDateTime(LocalDateTime.now());

                userRepository.save(user);
                log.debug("Updated successful login for user: {}", username);
            });
        } catch (Exception e) {
            log.error("Error updating successful login for user: {}", username, e);
        }
    }

    /**
     * Handle failed authentication attempt.
     *
     * @param username the username
     */
    @Transactional
    public void handleFailedAuthentication(String username) {
        try {
            userRepository.findByUsername(username).ifPresent(user -> {
                // Increment failed login attempts
                user.incrementFailedLoginAttempts();

                // Lock account if threshold exceeded
                if (user.shouldLockAccount()) {
                    user.setIsAccountLocked(true);
                    log.warn("Account locked due to excessive failed login attempts for user: {}", username);
                }

                userRepository.save(user);
                log.debug("Updated failed login attempt for user: {}", username);
            });
        } catch (Exception e) {
            log.error("Error updating failed login attempt for user: {}", username, e);
        }
    }

    /**
     * Check if user account is locked.
     *
     * @param username the username
     * @return true if account is locked
     */
    @Transactional(readOnly = true)
    public boolean isAccountLocked(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getIsAccountLocked() != null && user.getIsAccountLocked())
                .orElse(false);
    }

    /**
     * Check if user account is active.
     *
     * @param username the username
     * @return true if account is active
     */
    @Transactional(readOnly = true)
    public boolean isAccountActive(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getIsActive() != null && user.getIsActive())
                .orElse(false);
    }

    /**
     * Get user role.
     *
     * @param username the username
     * @return the role name
     */
    @Transactional(readOnly = true)
    public String getUserRole(String username) {
        return userRepository.findByUsername(username)
                .map(user -> user.getRole().name())
                .orElse(null);
    }

    /**
     * Get user by username.
     *
     * @param username the username
     * @return the user entity
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    /**
     * Check if user exists.
     *
     * @param username the username
     * @return true if user exists
     */
    @Transactional(readOnly = true)
    public boolean userExists(String username) {
        return userRepository.existsByUsername(username);
    }
}