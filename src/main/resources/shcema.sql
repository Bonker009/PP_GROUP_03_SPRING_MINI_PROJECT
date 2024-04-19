CREATE DATABASE expense_tracking;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE Users
(
    id            SERIAL       NOT NULL UNIQUE,
    user_id       uuid PRIMARY KEY DEFAULT uuid_generate_v4() ,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    profile_image VARCHAR(255)
);

CREATE TABLE Categories
(
    id          SERIAL       NOT NULL UNIQUE,
    category_id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    user_id     uuid,
    FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE
);

CREATE TABLE Expenses
(
    id          SERIAL         NOT NULL UNIQUE,
    expense_id  uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    amount      DECIMAL(10, 2) NOT NULL,
    description TEXT,
    date        DATE           NOT NULL,
    user_id     uuid,
    category_id uuid,
    FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE ,
    FOREIGN KEY (category_id) REFERENCES Categories (category_id) ON DELETE CASCADE
);

CREATE TABLE Otps
(
    id         SERIAL       NOT NULL UNIQUE,
    otp_id     uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    opt_code   VARCHAR(255) NOT NULL,
    issued_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    expiration TIMESTAMP    NOT NULL,
    verified   BOOLEAN      NOT NULL,
    user_id    uuid,
    FOREIGN KEY (user_id) REFERENCES Users (user_id) ON DELETE CASCADE
);
