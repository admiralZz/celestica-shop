-- changeset admin:008-1
ALTER TABLE users
    ADD COLUMN enabled BOOLEAN NOT NULL DEFAULT FALSE;

-- changeset admin:008-2
CREATE TABLE user_activation_token
(
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(255) NOT NULL UNIQUE,
    user_id     BIGINT       NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    expiry_date TIMESTAMP    NOT NULL,
    is_used     BOOLEAN      NOT NULL DEFAULT FALSE
);