--liquibase formatted sql

--changeset Kihtenko_Olga:011
CREATE TRIGGER check_update_person
    AFTER UPDATE ON person
    FOR EACH ROW
EXECUTE FUNCTION check_person_update_house();
