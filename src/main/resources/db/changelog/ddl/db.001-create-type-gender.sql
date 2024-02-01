--liquibase formatted sql

--changeset Kihtenko_Olga:001
CREATE TYPE gender AS ENUM ('MALE', 'FEMALE');
