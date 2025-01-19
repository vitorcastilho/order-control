ALTER TABLE customer_order
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'PENDING';

UPDATE customer_order
SET status = 'PENDING'
WHERE status IS NULL;