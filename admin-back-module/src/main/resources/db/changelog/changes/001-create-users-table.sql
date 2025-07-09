-- liquibase formatted sql

-- changeset admin:001
-- comment: Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'ROLE_USER' NOT NULL
);

CREATE INDEX idx_users_email ON users(email);