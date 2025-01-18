ALTER TABLE customer_order
    DROP COLUMN product_id,
    DROP COLUMN quantity;

ALTER TABLE customer_order
    RENAME COLUMN total_price TO total_order;