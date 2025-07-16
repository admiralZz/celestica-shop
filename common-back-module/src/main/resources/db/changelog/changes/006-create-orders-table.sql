-- liquibase formatted sql

-- changeset admin:006-1
CREATE TABLE orders
(
    id    BIGSERIAL PRIMARY KEY,
    email VARCHAR(128) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    address VARCHAR(1024) NOT NULL,
    total DECIMAL(10, 2) NOT NULL
);

CREATE INDEX idx_orders_email ON orders (email);
CREATE INDEX idx_orders_phone ON orders (phone);
CREATE INDEX idx_orders_address ON orders (address);

-- changeset admin:006-2
CREATE TABLE order_items
(
    id         BIGSERIAL PRIMARY KEY,
    product_id BIGINT REFERENCES products (id) ON DELETE SET NULL,
    order_id   BIGINT NOT NULL REFERENCES orders (id) ON DELETE CASCADE,
    quantity   INT            NOT NULL,
    total      DECIMAL(10, 2) NOT NULL
);

CREATE INDEX idx_order_items_product ON order_items(product_id);
CREATE INDEX idx_order_items_order ON order_items(order_id);

-- changeset admin:006-3
ALTER TABLE orders ADD user_id BIGINT;

-- changeset admin:006-4
ALTER TABLE orders
    ADD CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id)
            REFERENCES users(id) ON DELETE SET NULL;

-- changeset admin:006-5
CREATE INDEX idx_orders_user_id ON orders(user_id);