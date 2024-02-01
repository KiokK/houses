--liquibase formatted sql

--changeset Kihtenko_Olga:005
CREATE TABLE IF NOT EXISTS person_owner_house
(
    person_id BIGINT NOT NULL CONSTRAINT fk_person_house REFERENCES person (id),
    house_id  BIGINT NOT NULL CONSTRAINT fk_house_person REFERENCES house (id)
);
