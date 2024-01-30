CREATE TYPE gender AS ENUM ('MALE', 'FEMALE');
CREATE TYPE relation_house_type AS ENUM ('OWNER', 'TENANT', 'NOT_OWNER');

CREATE TABLE IF NOT EXISTS house
(
    id          BIGSERIAL PRIMARY KEY,
    uuid        UUID UNIQUE NOT NULL,
    area        REAL        NOT NULL,
    country     VARCHAR(30) NOT NULL,
    city        VARCHAR(30) NOT NULL,
    street      VARCHAR(30) NOT NULL,
    number      SMALLINT    NOT NULL,
    create_date TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS person
(
    id              BIGSERIAL PRIMARY KEY,
    uuid            UUID UNIQUE NOT NULL,
    name            VARCHAR(32) NOT NULL CHECK (name ~ '^[A-Za-z]+$'),
    surname         VARCHAR(32) NOT NULL CHECK (surname ~ '^[A-Za-z]+$'),
    sex             gender      NOT NULL,
    passport_series VARCHAR(2)  NOT NULL CHECK (passport_series ~ '^[A-Z]{2}$'),
    passport_number VARCHAR(7)  NOT NULL CHECK (passport_number ~ '^\d{7}$'),
    create_date     TIMESTAMP   NOT NULL,
    update_date     TIMESTAMP   NOT NULL,
    house_live_id   INT         NOT NULL,

    CONSTRAINT fk_persons_house_live_id FOREIGN KEY (house_live_id) REFERENCES house (id)
);

CREATE TABLE IF NOT EXISTS person_owner_house
(
    person_id BIGINT NOT NULL CONSTRAINT fk_person_house REFERENCES person (id),
    house_id  BIGINT NOT NULL CONSTRAINT fk_house_person REFERENCES house (id)
);

CREATE TABLE IF NOT EXISTS house_history
(
    person_id BIGINT               NOT NULL,
    house_id  BIGINT               NOT NULL,
    type      relation_house_type  NOT NULL,
    create_date TIMESTAMP   NOT NULL DEFAULT now()
);

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

CREATE OR REPLACE FUNCTION check_person_insert_house()
    RETURNS trigger AS $$
BEGIN
    INSERT INTO house_history(person_id, house_id, type) VALUES (NEW.id, NEW.house_live_id, 'TENANT');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION check_person_owner_house_insert()
    RETURNS trigger AS $$
BEGIN
    INSERT INTO house_history(person_id, house_id, type) VALUES (NEW.person_id, NEW.house_id, 'OWNER');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION check_person_owner_house_delete()
    RETURNS trigger AS $$
BEGIN
    INSERT INTO house_history(person_id, house_id, type) VALUES (OLD.person_id, OLD.house_id, 'NOT_OWNER');
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_update_person
    AFTER UPDATE ON person
    FOR EACH ROW
EXECUTE FUNCTION check_person_update_house();

CREATE TRIGGER check_insert_person
    AFTER INSERT ON person
    FOR EACH ROW
EXECUTE FUNCTION check_person_insert_house();

CREATE TRIGGER check_update_person_owner_house
    AFTER INSERT ON person_owner_house
    FOR EACH ROW
EXECUTE FUNCTION check_person_owner_house_insert();

CREATE TRIGGER check_delete_person_owner_house
    AFTER DELETE ON person_owner_house
    FOR EACH ROW
EXECUTE FUNCTION check_person_owner_house_delete();
