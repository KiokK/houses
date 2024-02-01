--liquibase formatted sql

--changeset Kihtenko_Olga:008 splitStatements:false
CREATE OR REPLACE FUNCTION check_person_insert_house()
    RETURNS trigger AS $$
BEGIN
    INSERT INTO house_history(person_id, house_id, type) VALUES (NEW.id, NEW.house_live_id, 'TENANT');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
