CREATE SCHEMA IF NOT EXISTS inventory AUTHORIZATION admin;

DROP TABLE IF EXISTS inventory.product_color CASCADE;
CREATE TABLE inventory.product_color 
(
    id SERIAL,
    product_skucode VARCHAR(10) NOT NULL,
    color_hex VARCHAR(10)NOT NULL,
    PRIMARY KEY (id)
);


DROP TABLE IF EXISTS inventory.inventory CASCADE;
CREATE TABLE inventory.inventory 
(
    id SERIAL,
    product_color_id INT references inventory.product_color (id),
    quantity INT,
    PRIMARY KEY(id)
);

DROP TABLE IF EXISTS inventory.item_lines CASCADE;
CREATE TABLE inventory.item_lines 
(
    id SERIAL,
    product_color_id INT references inventory.product_color (id),
    quantity INT,
    price DECIMAL,
    is_ordered BOOLEAN,
    PRIMARY KEY(id)
);
