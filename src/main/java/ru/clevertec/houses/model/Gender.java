package ru.clevertec.houses.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {

    MALE("MALE"),
    FEMALE("FEMALE");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Gender fromValue(String value) {
        if (value.equalsIgnoreCase("Male")) {
            return MALE;
        }
        if (value.equalsIgnoreCase("Female")) {
            return FEMALE;
        }

        return valueOf(value.toUpperCase());
    }

}
