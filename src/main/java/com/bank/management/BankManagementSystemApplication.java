package com.bank.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Main Spring Boot application class for Bank Management System.
 *
 * This application provides a complete banking solution with features like:
 * - User authentication and authorization
 * - Account management
 * - Transaction processing
 * - Admin dashboard
 * - RESTful API endpoints
 *
 * @author Senior Java Full Stack Developer
 * @version 1.0.0
 * @since 2026
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableAsync
public class BankManagementSystemApplication {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(BankManagementSystemApplication.class, args);
    }
}