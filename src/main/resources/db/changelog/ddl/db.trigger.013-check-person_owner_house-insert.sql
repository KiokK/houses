--liquibase formatted sql

--changeset Kihtenko_Olga:013
CREATE TRIGGER check_update_person_owner_house
    AFTER INSERT ON person_owner_house
    FOR EACH ROW
EXECUTE FUNCTION check_person_owner_house_insert();
