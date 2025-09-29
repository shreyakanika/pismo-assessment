-- ==============================================
-- Pismo Code Assessment - DDL (Data Definition Language)
-- Script to create all necessary tables.
-- ==============================================
DROP TABLE IF EXISTS transaction;
-- 1. ACCOUNT Table
-- Stores customer account information.
-- ACCOUNT_ID is the primary key.
-- DOCUMENT_NUMBER must be unique to ensure one account per document.
DROP TABLE IF EXISTS account;
CREATE TABLE account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    document_number VARCHAR(255) NOT NULL UNIQUE
);

-- 2. OPERATION_TYPE Table
-- Stores the lookup values for transaction types (Purchase, Payment, etc.).
-- Data for this table should be loaded via data.sql.
DROP TABLE IF EXISTS operation_type;
CREATE TABLE operation_type (
    id BIGINT PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

-- 3. TRANSACTION Table
-- Stores transaction records linked to an account and an operation type.
DROP TABLE IF EXISTS transaction;
CREATE TABLE transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_id BIGINT NOT NULL,
    operation_type_id BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL, -- Use DECIMAL for precise currency handling
    event_date TIMESTAMP NOT NULL,  -- Records the date and time of the transaction

    -- Define Foreign Key Constraints
    CONSTRAINT fk_transaction_account
        FOREIGN KEY (account_id)
        REFERENCES account(id),

    CONSTRAINT fk_transaction_operation_type
        FOREIGN KEY (operation_type_id)
        REFERENCES operation_type(id)
);
