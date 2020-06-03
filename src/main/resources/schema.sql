CREATE TABLE IF NOT EXISTS users
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(250) NOT NULL,
    balance BIGINT       NOT NULL
);

CREATE TABLE IF NOT EXISTS transaction
(
    transaction_id    BIGINT PRIMARY KEY,
    created_timestamp BIGINT,
    user_id           BIGINT
);
