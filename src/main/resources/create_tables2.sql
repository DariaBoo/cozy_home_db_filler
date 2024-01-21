--CREATE SCHEMA IF NOT EXISTS inventory AUTHORIZATION admin;

DROP TABLE IF EXISTS product_color CASCADE;
CREATE TABLE product_color 
(
    id SERIAL,
    product_skucode VARCHAR(10) NOT NULL,
    color_hex VARCHAR(10)NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS basket_items CASCADE;
CREATE TABLE basket_items
(
    id SERIAL,
    product_color_id INT references product_color (id),
    quantity INT,
    user_id VARCHAR NOT NULL,
    PRIMARY KEY(id)
);
