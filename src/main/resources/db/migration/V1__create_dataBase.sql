DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS meals;

CREATE TABLE users
(
    id               BIGINT PRIMARY KEY,
    username         VARCHAR(100)            NOT NULL,
    email            VARCHAR(100)            NOT NULL,
    password         VARCHAR(100)            NOT NULL,
    active           BOOLEAN                 NOT NULL,
    activation_code  VARCHAR(255),
    registered       TIMESTAMP DEFAULT NOW() NOT NULL,
    calories_per_day Integer   DEFAULT 2000
);

CREATE TABLE meals
(
    id          BIGINT PRIMARY KEY,
    date_time   TIMESTAMP NOT NULL,
    description VARCHAR(120),
    calories    INTEGER   NOT NULL,
    user_id     BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE user_role
(
    user_id BIGINT NOT NULL,
    role VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);