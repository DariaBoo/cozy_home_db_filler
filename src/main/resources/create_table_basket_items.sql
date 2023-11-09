DROP TABLE IF EXISTS basket_items CASCADE;
CREATE TABLE basket_items
(
    id SERIAL,
    product_color_id INT references product_color (id),
    quantity INT,
    user_id VARCHAR NOT NULL,
    PRIMARY KEY(id)
);
