-- liquibase formatted sql

-- changeset admin:010-1
CREATE TABLE mail_settings
(
    id         BIGSERIAL PRIMARY KEY,
    host       VARCHAR(1024) NOT NULL,
    port       INT           NOT NULL,
    username   VARCHAR(256)  NOT NULL,
    password   VARCHAR(1024) NOT NULL,
    protocol   VARCHAR(64)   NOT NULL,
    auth       BOOLEAN       NOT NULL,
    ssl_enable BOOLEAN       NOT NULL
);