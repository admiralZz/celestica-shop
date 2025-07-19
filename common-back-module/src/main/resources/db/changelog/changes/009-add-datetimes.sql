-- changeset admin:009-1
ALTER TABLE users
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- changeset admin:009-2
ALTER TABLE user_activation_token
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- changeset admin:009-3
ALTER TABLE products
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- changeset admin:009-4
ALTER TABLE products
    ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- changeset admin:009-5
ALTER TABLE orders
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- changeset admin:009-6
ALTER TABLE categories
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;