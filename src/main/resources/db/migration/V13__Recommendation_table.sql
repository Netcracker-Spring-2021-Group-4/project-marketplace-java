CREATE TABLE IF NOT EXISTS product_recommendation
(
    product_id        uuid    ,
    other_product_id        uuid ,
    lift               numeric,
    PRIMARY KEY(product_id, other_product_id),


    CONSTRAINT recommendation_product_fk FOREIGN KEY (product_id) REFERENCES product (product_id)
        ON update CASCADE ON delete CASCADE,
    CONSTRAINT recommended_product_fk FOREIGN KEY (other_product_id) REFERENCES product (product_id)
        ON update CASCADE ON delete CASCADE
);