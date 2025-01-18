CREATE TABLE order_item (
    id SERIAL PRIMARY KEY,
    customer_order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    unit_price NUMERIC(10, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    total_item_price NUMERIC(10, 2) NOT NULL,
    CONSTRAINT fk_customer_order FOREIGN KEY (customer_order_id) REFERENCES customer_order (id) ON DELETE CASCADE,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE
);