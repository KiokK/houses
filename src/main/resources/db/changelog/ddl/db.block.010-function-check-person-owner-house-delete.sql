--liquibase formatted sql

--changeset Kihtenko_Olga:010 splitStatements:false
CREATE OR REPLACE FUNCTION check_person_owner_house_delete()
    RETURNS trigger AS $$
BEGIN
    INSERT INTO house_history(person_id, house_id, type) VALUES (OLD.person_id, OLD.house_id, 'NOT_OWNER');
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;
