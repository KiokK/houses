--liquibase formatted sql

--changeset Kihtenko_Olga:002
CREATE TYPE relation_house_type AS ENUM ('OWNER', 'TENANT', 'NOT_OWNER');
