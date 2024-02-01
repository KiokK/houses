--liquibase formatted sql

--changeset Kihtenko_Olga:012
CREATE TRIGGER check_insert_person
    AFTER INSERT ON person
    FOR EACH ROW
EXECUTE FUNCTION check_person_insert_house();
