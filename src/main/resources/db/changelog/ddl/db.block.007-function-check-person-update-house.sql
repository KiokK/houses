--liquibase formatted sql

--changeset Kihtenko_Olga:007 splitStatements:false
CREATE OR REPLACE FUNCTION check_person_update_house()
    RETURNS trigger AS $$
BEGIN
    IF NEW.house_live_id != OLD.house_live_id THEN
        BEGIN
            INSERT INTO house_history(person_id, house_id, type) VALUES (NEW.id, NEW.house_live_id, 'TENANT');
        END;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
