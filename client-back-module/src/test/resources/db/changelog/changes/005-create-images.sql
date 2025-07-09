-- liquibase formatted sql

-- changeset admin:005-1
CREATE TABLE image(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(128)
);

-- changeset admin:005-2
ALTER TABLE products ADD image_id BIGINT;

-- changeset admin:005-3
ALTER TABLE products
    ADD CONSTRAINT fk_products_image
        FOREIGN KEY (image_id)
            REFERENCES image(id);