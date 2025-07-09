-- liquibase formatted sql

-- changeset admin:004-1
-- comment: Insert admin user
INSERT INTO users (email, password, role)
VALUES ('admin@example.com', '$2a$10$MtURe4ssql3iTjOux5RBhe3nWrqLDgjrw3xMjJjhc.JnjTPovlK7G', 'ROLE_ADMIN');