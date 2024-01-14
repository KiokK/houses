drop table if exists person_owner_house;
drop table if exists person;
drop table if exists house;

CREATE TABLE IF NOT EXISTS house
(
    id          BIGSERIAL PRIMARY KEY,
    uuid        UUID UNIQUE NOT NULL,
    area        REAL        NOT NULL,
    country     VARCHAR(30) NOT NULL,
    city        VARCHAR(30) NOT NULL,
    street      VARCHAR(30) NOT NULL,
    number      SMALLINT    NOT NULL,
    create_date TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS person
(
    id              BIGSERIAL PRIMARY KEY,
    uuid            UUID UNIQUE NOT NULL,
    name            VARCHAR(32) NOT NULL CHECK (name ~ '^[A-Za-z]+$'),
    surname         VARCHAR(32) NOT NULL CHECK (surname ~ '^[A-Za-z]+$'),
    sex             VARCHAR(6)  NOT NULL CHECK (name ~ '^[A-Za-z]+$'),
    passport_series VARCHAR(2)  NOT NULL CHECK (passport_series ~ '^[A-Z]{2}$'),
    passport_number VARCHAR(7)  NOT NULL CHECK (passport_number ~ '^\d{7}$'),
    create_date     TIMESTAMP   NOT NULL,
    update_date     TIMESTAMP   NOT NULL,
    house_live_id   INT         NOT NULL,

    CONSTRAINT fk_persons_house_live_id FOREIGN KEY (house_live_id) REFERENCES house (id)
);

CREATE TABLE IF NOT EXISTS person_owner_house
(
    person_id BIGINT NOT NULL CONSTRAINT fk_person_house REFERENCES person (id),
    house_id  BIGINT NOT NULL CONSTRAINT fk_house_person REFERENCES house (id)
);
