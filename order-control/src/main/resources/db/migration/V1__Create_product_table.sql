CREATE TABLE product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    units INTEGER NOT NULL,
    price NUMERIC(10, 2) NOT NULL
);