package com.bank.management.config;

import com.bank.management.entity.User;
import com.bank.management.entity.UserSession;
import com.bank.management.repository.UserRepository;
import com.bank.management.repository.UserSessionRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * JWT authentication filter that intercepts requests to validate JWT tokens.
 *
 * This filter checks for JWT tokens in the Authorization header, validates them,
 * and sets the authentication context for Spring Security.
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = getJwtFromRequest(request);

            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
                String username = jwtTokenProvider.getUsernameFromToken(jwt);
                Long userId = jwtTokenProvider.getUserIdFromToken(jwt);

                // Verify user exists and is active
                Optional<User> userOptional = userRepository.findByUsername(username);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();

                    // Check if user is active and not locked
                    if (user.getIsActive() && !user.getIsAccountLocked()) {

                        // Verify session is still active (optional, for additional security)
                        String sessionToken = extractSessionToken(jwt);
                        if (sessionToken != null) {
                            Optional<UserSession> sessionOptional = userSessionRepository.findActiveBySessionToken(sessionToken);
                            if (sessionOptional.isPresent()) {
                                UserSession session = sessionOptional.get();

                                // Check if session belongs to the user
                                if (session.getUser().getId().equals(userId)) {
                                    // Update last activity if needed
                                    updateSessionActivity(session);

                                    // Create authentication token
                                    UserDetails userDetails = user;
                                    UsernamePasswordAuthenticationToken authentication =
                                            new UsernamePasswordAuthenticationToken(
                                                    userDetails, null, userDetails.getAuthorities());

                                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                                    // Set authentication in security context
                                    SecurityContextHolder.getContext().setAuthentication(authentication);

                                    log.debug("Set authentication for user: {}", username);
                                } else {
                                    log.warn("Session token does not match user: {}", username);
                                }
                            } else {
                                log.warn("Session not found or inactive for token");
                            }
                        } else {
                            // Fallback: create authentication without session validation
                            UserDetails userDetails = user;
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails, null, userDetails.getAuthorities());

                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);

                            log.debug("Set authentication for user (no session validation): {}", username);
                        }
                    } else {
                        log.warn("User account is inactive or locked: {}", username);
                    }
                } else {
                    log.warn("User not found for JWT token: {}", username);
                }
            }
        } catch (Exception ex) {
            log.error("Could not set user authentication in security context", ex);
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT token from Authorization header.
     *
     * @param request the HTTP request
     * @return the JWT token or null
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    /**
     * Extract session token from JWT (if embedded).
     *
     * @param jwt the JWT token
     * @return the session token or null
     */
    private String extractSessionToken(String jwt) {
        try {
            // For now, return null as we're not embedding session tokens in JWT
            // This can be enhanced to include session validation in JWT claims
            return null;
        } catch (Exception e) {
            log.debug("Could not extract session token from JWT", e);
            return null;
        }
    }

    /**
     * Update session activity timestamp.
     *
     * @param session the user session
     */
    private void updateSessionActivity(UserSession session) {
        // This could be enhanced to track last activity time
        // For now, we just validate the session exists and is active
        log.trace("Session validated for user: {}", session.getUser().getUsername());
    }

    /**
     * Check if the request should be filtered.
     * Skip filtering for authentication endpoints and static resources.
     *
     * @param request the HTTP request
     * @return true if should be filtered
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Skip authentication endpoints
        if (path.startsWith("/api/v1/auth/")) {
            return true;
        }

        // Skip static resources
        if (path.startsWith("/css/") || path.startsWith("/js/") ||
            path.startsWith("/images/") || path.startsWith("/favicon.ico")) {
            return true;
        }

        // Skip actuator endpoints (optional)
        if (path.startsWith("/actuator/")) {
            return true;
        }

        // Skip OPTIONS requests (for CORS preflight)
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        return false;
    }
}