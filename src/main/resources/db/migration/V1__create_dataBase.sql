CREATE TABLE users
(
    id               BIGSERIAL               NOT NULL,
    username         VARCHAR(100)            NOT NULL UNIQUE,
    email            VARCHAR(100)            NOT NULL UNIQUE,
    password         VARCHAR(100)            NOT NULL,
    active           BOOLEAN                 NOT NULL,
    activation_code  VARCHAR(255),
    registered       TIMESTAMP DEFAULT NOW() NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE meals
(
    id          BIGSERIAL    NOT NULL,
    date_time   TIMESTAMP NOT NULL,
    description VARCHAR(120),
    calories    INTEGER   NOT NULL,
    user_id     BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE user_role
(
    user_id BIGINT NOT NULL,
    role    VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);