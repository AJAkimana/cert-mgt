WITH new_customer AS (
    INSERT INTO customers (name, email)
    VALUES ('Test Customer', 'test@gmail.com')
    RETURNING id
)
INSERT INTO users (customer_id, username, email, password_hash, role)
SELECT id, 'test', 'test@gmail.com', '$2a$10$rx3FVC1h9FXrt6rQRJ.Y3e2Dc.qG0rnfx0JRLFs57EnDJ7hfW4Dfy', 'MEMBER'
FROM new_customer;
