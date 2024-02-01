--liquibase formatted sql

--changeset Kihtenko_Olga:003
CREATE TABLE IF NOT EXISTS house
(
    id          BIGSERIAL PRIMARY KEY,
    uuid        UUID UNIQUE NOT NULL,
    area        REAL        NOT NULL,
    country     VARCHAR(30) NOT NULL,
    city        VARCHAR(30) NOT NULL,
    street      VARCHAR(30) NOT NULL,
    number      SMALLINT    NOT NULL,
    create_date TIMESTAMP   NOT NULL DEFAULT now()
);
