DELETE FROM operation_type;

-- Insert the required operation types (IDs 1 through 4)
INSERT INTO operation_type (id, description) VALUES (1, 'PURCHASE');
INSERT INTO operation_type (id, description) VALUES (2, 'INSTALLMENT PURCHASE');
INSERT INTO operation_type (id, description) VALUES (3, 'WITHDRAWAL');
INSERT INTO operation_type (id, description) VALUES (4, 'PAYMENT');
