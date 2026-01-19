-- Bank Management System Database Schema
-- MySQL 8.0+ Compatible
-- Author: Senior Java Full Stack Developer
-- Created: January 2026

-- Create database
CREATE DATABASE IF NOT EXISTS bank_management_system
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE bank_management_system;

-- ============================================================================
-- TABLE: users
-- Description: Stores user information for both customers and administrators
-- ============================================================================
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone_number VARCHAR(15),
    date_of_birth DATE,
    address TEXT,
    role ENUM('ADMIN', 'CUSTOMER') NOT NULL DEFAULT 'CUSTOMER',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    is_account_locked BOOLEAN NOT NULL DEFAULT FALSE,
    failed_login_attempts INT NOT NULL DEFAULT 0,
    last_login_datetime DATETIME,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Indexes for performance
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_is_active (is_active),
    INDEX idx_created_at (created_at),

    -- Constraints
    CONSTRAINT chk_username_length CHECK (CHAR_LENGTH(username) >= 3),
    CONSTRAINT chk_email_format CHECK (email REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'),
    CONSTRAINT chk_phone_format CHECK (phone_number REGEXP '^[0-9+\-\s()]+$')
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- TABLE: accounts
-- Description: Stores bank account information for customers
-- ============================================================================
CREATE TABLE accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    account_type ENUM('SAVINGS', 'CURRENT', 'FIXED_DEPOSIT') NOT NULL DEFAULT 'SAVINGS',
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'INR',
    status ENUM('ACTIVE', 'BLOCKED', 'CLOSED') NOT NULL DEFAULT 'ACTIVE',
    branch_name VARCHAR(100),
    ifsc_code VARCHAR(11),
    opened_date DATE NOT NULL DEFAULT (CURRENT_DATE),
    closed_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign key constraint
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    -- Indexes for performance
    INDEX idx_account_number (account_number),
    INDEX idx_user_id (user_id),
    INDEX idx_account_type (account_type),
    INDEX idx_status (status),
    INDEX idx_opened_date (opened_date),

    -- Constraints
    CONSTRAINT chk_balance_non_negative CHECK (balance >= 0),
    CONSTRAINT chk_ifsc_format CHECK (ifsc_code REGEXP '^[A-Z]{4}[0-9]{7}$'),
    CONSTRAINT chk_closed_date_after_opened CHECK (closed_date IS NULL OR closed_date >= opened_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- TABLE: transactions
-- Description: Stores all financial transactions (deposits, withdrawals, transfers)
-- ============================================================================
CREATE TABLE transactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_id VARCHAR(36) NOT NULL UNIQUE, -- UUID for external reference
    account_id BIGINT NOT NULL,
    transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER_IN', 'TRANSFER_OUT') NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    balance_before DECIMAL(15,2) NOT NULL,
    balance_after DECIMAL(15,2) NOT NULL,
    description TEXT,
    transaction_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reference_account_id BIGINT, -- For transfers (source/destination account)
    transfer_reference VARCHAR(36), -- Links transfer in/out transactions
    transaction_status ENUM('PENDING', 'COMPLETED', 'FAILED', 'CANCELLED') NOT NULL DEFAULT 'COMPLETED',
    failure_reason TEXT,
    created_by BIGINT, -- User who initiated the transaction
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key constraints
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
    FOREIGN KEY (reference_account_id) REFERENCES accounts(id) ON DELETE SET NULL,
    FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE SET NULL,

    -- Indexes for performance
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_account_id (account_id),
    INDEX idx_transaction_type (transaction_type),
    INDEX idx_transaction_date (transaction_date),
    INDEX idx_transaction_status (transaction_status),
    INDEX idx_transfer_reference (transfer_reference),
    INDEX idx_created_by (created_by),

    -- Constraints
    CONSTRAINT chk_amount_positive CHECK (amount > 0),
    CONSTRAINT chk_balance_consistency CHECK (balance_after = balance_before + (CASE
        WHEN transaction_type IN ('DEPOSIT', 'TRANSFER_IN') THEN amount
        WHEN transaction_type IN ('WITHDRAWAL', 'TRANSFER_OUT') THEN -amount
        ELSE 0
    END))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- TABLE: user_sessions
-- Description: Tracks user login sessions for security and auditing
-- ============================================================================
CREATE TABLE user_sessions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    session_token VARCHAR(255) NOT NULL UNIQUE,
    ip_address VARCHAR(45), -- IPv4/IPv6 support
    user_agent TEXT,
    login_datetime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    logout_datetime DATETIME,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key constraint
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,

    -- Indexes for performance
    INDEX idx_user_id (user_id),
    INDEX idx_session_token (session_token),
    INDEX idx_is_active (is_active),
    INDEX idx_login_datetime (login_datetime),

    -- Constraints
    CONSTRAINT chk_logout_after_login CHECK (logout_datetime IS NULL OR logout_datetime >= login_datetime)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- VIEWS: For common queries and reporting
-- ============================================================================

-- View: Active customer accounts with user details
CREATE OR REPLACE VIEW active_customer_accounts AS
SELECT
    a.id AS account_id,
    a.account_number,
    a.account_type,
    a.balance,
    a.currency,
    a.status,
    a.opened_date,
    u.id AS user_id,
    u.username,
    u.email,
    u.first_name,
    u.last_name,
    u.phone_number
FROM accounts a
JOIN users u ON a.user_id = u.id
WHERE a.status = 'ACTIVE'
  AND u.role = 'CUSTOMER'
  AND u.is_active = TRUE;

-- View: Recent transactions summary
CREATE OR REPLACE VIEW recent_transactions AS
SELECT
    t.id,
    t.transaction_id,
    t.transaction_type,
    t.amount,
    t.balance_after,
    t.transaction_date,
    t.description,
    a.account_number,
    u.username,
    u.first_name,
    u.last_name
FROM transactions t
JOIN accounts a ON t.account_id = a.id
JOIN users u ON a.user_id = u.id
WHERE t.transaction_date >= DATE_SUB(CURRENT_DATE, INTERVAL 30 DAY)
ORDER BY t.transaction_date DESC;

-- ============================================================================
-- STORED PROCEDURES: For complex business logic
-- ============================================================================

-- Procedure: Transfer money between accounts
DELIMITER //

CREATE PROCEDURE transfer_money(
    IN p_from_account_id BIGINT,
    IN p_to_account_id BIGINT,
    IN p_amount DECIMAL(15,2),
    IN p_description TEXT,
    IN p_created_by BIGINT,
    OUT p_transaction_id VARCHAR(36),
    OUT p_status VARCHAR(20),
    OUT p_message TEXT
)
BEGIN
    DECLARE v_from_balance DECIMAL(15,2);
    DECLARE v_to_balance DECIMAL(15,2);
    DECLARE v_transfer_ref VARCHAR(36);
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SET p_status = 'FAILED';
        SET p_message = 'Transaction failed due to database error';
        ROLLBACK;
    END;

    START TRANSACTION;

    -- Check if accounts exist and are active
    SELECT balance INTO v_from_balance
    FROM accounts
    WHERE id = p_from_account_id AND status = 'ACTIVE' FOR UPDATE;

    SELECT balance INTO v_to_balance
    FROM accounts
    WHERE id = p_to_account_id AND status = 'ACTIVE' FOR UPDATE;

    IF v_from_balance IS NULL THEN
        SET p_status = 'FAILED';
        SET p_message = 'Source account not found or inactive';
        ROLLBACK;
    ELSEIF v_to_balance IS NULL THEN
        SET p_status = 'FAILED';
        SET p_message = 'Destination account not found or inactive';
        ROLLBACK;
    ELSEIF v_from_balance < p_amount THEN
        SET p_status = 'FAILED';
        SET p_message = 'Insufficient balance';
        ROLLBACK;
    ELSE
        -- Generate transfer reference
        SET v_transfer_ref = UUID();

        -- Debit from source account
        UPDATE accounts SET balance = balance - p_amount
        WHERE id = p_from_account_id;

        -- Credit to destination account
        UPDATE accounts SET balance = balance + p_amount
        WHERE id = p_to_account_id;

        -- Record debit transaction
        INSERT INTO transactions (
            transaction_id, account_id, transaction_type, amount,
            balance_before, balance_after, description, reference_account_id,
            transfer_reference, created_by
        ) VALUES (
            UUID(), p_from_account_id, 'TRANSFER_OUT', p_amount,
            v_from_balance, v_from_balance - p_amount, p_description, p_to_account_id,
            v_transfer_ref, p_created_by
        );

        -- Record credit transaction
        INSERT INTO transactions (
            transaction_id, account_id, transaction_type, amount,
            balance_before, balance_after, description, reference_account_id,
            transfer_reference, created_by
        ) VALUES (
            UUID(), p_to_account_id, 'TRANSFER_IN', p_amount,
            v_to_balance, v_to_balance + p_amount, p_description, p_from_account_id,
            v_transfer_ref, p_created_by
        );

        SET p_transaction_id = v_transfer_ref;
        SET p_status = 'SUCCESS';
        SET p_message = 'Transfer completed successfully';
        COMMIT;
    END IF;
END //

DELIMITER ;

-- ============================================================================
-- TRIGGERS: For audit and data consistency
-- ============================================================================

-- Trigger: Generate account number automatically
DELIMITER //

CREATE TRIGGER generate_account_number
BEFORE INSERT ON accounts
FOR EACH ROW
BEGIN
    DECLARE next_num INT;
    DECLARE account_num VARCHAR(20);

    -- Get next account number (starting from 1000000001)
    SELECT COALESCE(MAX(CAST(SUBSTRING(account_number, 2) AS UNSIGNED)), 1000000000) + 1
    INTO next_num
    FROM accounts;

    SET account_num = CONCAT('A', LPAD(next_num, 9, '0'));
    SET NEW.account_number = account_num;
END //

-- Trigger: Log balance changes for auditing
CREATE TRIGGER audit_balance_changes
AFTER UPDATE ON accounts
FOR EACH ROW
BEGIN
    IF OLD.balance != NEW.balance THEN
        INSERT INTO transaction_audit (
            account_id,
            old_balance,
            new_balance,
            change_amount,
            change_type,
            changed_at
        ) VALUES (
            NEW.id,
            OLD.balance,
            NEW.balance,
            NEW.balance - OLD.balance,
            CASE
                WHEN NEW.balance > OLD.balance THEN 'CREDIT'
                ELSE 'DEBIT'
            END,
            NOW()
        );
    END IF;
END //

DELIMITER ;

-- ============================================================================
-- AUDIT TABLE: For balance change tracking
-- ============================================================================
CREATE TABLE transaction_audit (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT NOT NULL,
    old_balance DECIMAL(15,2) NOT NULL,
    new_balance DECIMAL(15,2) NOT NULL,
    change_amount DECIMAL(15,2) NOT NULL,
    change_type ENUM('CREDIT', 'DEBIT') NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
    INDEX idx_account_id (account_id),
    INDEX idx_changed_at (changed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================================
-- SAMPLE DATA INSERTION
-- ============================================================================

-- Insert sample admin user
INSERT INTO users (username, email, password, first_name, last_name, phone_number, role, is_active)
VALUES ('admin', 'admin@bank.com', '$2a$10$xHcLpJCgzFZQvLNZbAYwWeF5T3n6r1g8KjYzKjYzKjYzKjYzKjYzK', 'System', 'Administrator', '+91-9999999999', 'ADMIN', TRUE);

-- Insert sample customers
INSERT INTO users (username, email, password, first_name, last_name, phone_number, date_of_birth, address, role, is_active)
VALUES
('john_doe', 'john.doe@email.com', '$2a$10$xHcLpJCgzFZQvLNZbAYwWeF5T3n6r1g8KjYzKjYzKjYzKjYzKjYzK', 'John', 'Doe', '+91-9876543210', '1990-05-15', '123 Main St, Mumbai, India', 'CUSTOMER', TRUE),
('jane_smith', 'jane.smith@email.com', '$2a$10$xHcLpJCgzFZQvLNZbAYwWeF5T3n6r1g8KjYzKjYzKjYzKjYzKjYzK', 'Jane', 'Smith', '+91-9876543211', '1985-08-20', '456 Oak Ave, Delhi, India', 'CUSTOMER', TRUE),
('bob_wilson', 'bob.wilson@email.com', '$2a$10$xHcLpJCgzFZQvLNZbAYwWeF5T3n6r1g8KjYzKjYzKjYzKjYzKjYzKjYzK', 'Bob', 'Wilson', '+91-9876543212', '1992-03-10', '789 Pine Rd, Bangalore, India', 'CUSTOMER', TRUE);

-- Note: Password for all users is 'password123' (BCrypt hashed)
-- In production, use a proper password hashing service

COMMIT;