DROP TABLE IF EXISTS favorite_products CASCADE;
CREATE TABLE favorite_products
(
    id SERIAL,
    product_color_id INT references product_color (id),
    user_id VARCHAR NOT NULL,
    PRIMARY KEY(id)
);
