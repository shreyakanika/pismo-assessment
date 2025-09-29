
DROP TABLE IF EXISTS transaction;

DROP TABLE IF EXISTS account;
CREATE TABLE account (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    document_number VARCHAR(255) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS operation_type;
CREATE TABLE operation_type (
    id BIGINT PRIMARY KEY,
    description VARCHAR(255) NOT NULL
);

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
