CREATE TABLE customer_order (
    id SERIAL PRIMARY KEY,
    number_order VARCHAR(50) NOT NULL UNIQUE,
    customer_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INTEGER NOT NULL,
    total_price NUMERIC(10, 2) NOT NULL,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer (id) ON DELETE CASCADE,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);