-- liquibase formatted sql

-- changeset admin:011-1
CREATE TABLE partnership_request
(
    id         BIGSERIAL PRIMARY KEY,
    first_name       VARCHAR(1024) NOT NULL,
    last_name       VARCHAR(1024) NOT NULL,
    company       VARCHAR(1024) NOT NULL,
    email       VARCHAR(1024) NOT NULL,
    phone       VARCHAR(1024) DEFAULT NULL,
    location       VARCHAR(1024) DEFAULT NULL,
    cooperation_type       VARCHAR(1024) NOT NULL,
    product_category       VARCHAR(1024) DEFAULT NULL,
    message       VARCHAR(1024) DEFAULT NULL
);