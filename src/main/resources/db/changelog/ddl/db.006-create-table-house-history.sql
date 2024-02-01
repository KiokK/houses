--liquibase formatted sql

--changeset Kihtenko_Olga:006
CREATE TABLE IF NOT EXISTS house_history
(
    person_id BIGINT               NOT NULL,
    house_id  BIGINT               NOT NULL,
    type      relation_house_type  NOT NULL,
    create_date TIMESTAMP   NOT NULL DEFAULT now()
);
