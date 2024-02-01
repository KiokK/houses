--liquibase formatted sql

--changeset Kihtenko_Olga:004
CREATE TABLE IF NOT EXISTS person
(
    id              BIGSERIAL PRIMARY KEY,
    uuid            UUID UNIQUE NOT NULL,
    name            VARCHAR(32) NOT NULL CHECK (name ~ '^[A-Za-z]+$'),
    surname         VARCHAR(32) NOT NULL CHECK (surname ~ '^[A-Za-z]+$'),
    sex             gender      NOT NULL,
    passport_series VARCHAR(2)  NOT NULL CHECK (passport_series ~ '^[A-Z]{2}$'),
    passport_number VARCHAR(7)  NOT NULL CHECK (passport_number ~ '^\d{7}$'),
    create_date     TIMESTAMP   NOT NULL DEFAULT now(),
    update_date     TIMESTAMP   NOT NULL DEFAULT now(),
    house_live_id   INT         NOT NULL,

    CONSTRAINT fk_persons_house_live_id FOREIGN KEY (house_live_id) REFERENCES house (id)
);
