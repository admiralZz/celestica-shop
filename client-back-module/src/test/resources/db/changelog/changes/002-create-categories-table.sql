-- liquibase formatted sql

-- changeset admin:002
-- comment: Create categories table
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE INDEX idx_categories_name ON categories(name);