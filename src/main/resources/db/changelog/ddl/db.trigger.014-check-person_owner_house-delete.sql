--liquibase formatted sql

--changeset Kihtenko_Olga:014
CREATE TRIGGER check_delete_person_owner_house
    AFTER DELETE ON person_owner_house
    FOR EACH ROW
EXECUTE FUNCTION check_person_owner_house_delete();
