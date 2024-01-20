package ru.clevertec.houses.util;

import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.model.Gender;

import java.util.UUID;

public class PersonDtoTestData {

    public static PersonDto.PersonDtoBuilder validPersonBuilder() {
        return PersonDto.builder()
                .name("Ivan")
                .surname("Ivanov")
                .sex(Gender.MALE)
                .uuid(UUID.randomUUID())
                .houseLiveUuid(UUID.randomUUID())
                .passportNumber("1234567")
                .passportSeries("QK");
    }

}
