--CREATE SCHEMA IF NOT EXISTS inventory AUTHORIZATION admin;

DROP TABLE IF EXISTS product_color CASCADE;
CREATE TABLE product_color 
(
    id SERIAL,
    product_skucode VARCHAR(10) NOT NULL,
    color_hex VARCHAR(10)NOT NULL,
    PRIMARY KEY (id)
);


DROP TABLE IF EXISTS inventory CASCADE;
CREATE TABLE inventory 
(
    id SERIAL,
    product_color_id INT references product_color (id),
    quantity INT,
    PRIMARY KEY(id)
);

DROP TABLE IF EXISTS item_lines CASCADE;
CREATE TABLE item_lines 
(
    id SERIAL,
    product_color_id INT references product_color (id),
    quantity INT,
    price DECIMAL,
    is_ordered BOOLEAN,
    PRIMARY KEY(id)
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

DROP TABLE IF EXISTS favorite_products CASCADE;
CREATE TABLE favorite_products
(
    id SERIAL,
    product_skucode VARCHAR(10) NOT NULL,
    user_id VARCHAR NOT NULL,
    PRIMARY KEY(id)
);

DROP TABLE IF EXISTS reviews;
CREATE TABLE reviews (
id uuid NOT NULL PRIMARY KEY,
rating  int  CHECK ( rating > 0 AND rating < 6),
comment varchar(500) NOT NULL,
created_at timestamp NOT NULL,
modified_at timestamp NOT NULL,
user_id varchar(40) NOT NULL,
product_sku_code varchar(6) NOT NULL
);
