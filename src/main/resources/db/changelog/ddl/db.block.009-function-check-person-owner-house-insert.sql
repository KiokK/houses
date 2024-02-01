--liquibase formatted sql

--changeset Kihtenko_Olga:009 splitStatements:false
CREATE OR REPLACE FUNCTION check_person_owner_house_insert()
    RETURNS trigger AS $$
BEGIN
    INSERT INTO house_history(person_id, house_id, type) VALUES (NEW.person_id, NEW.house_id, 'OWNER');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
