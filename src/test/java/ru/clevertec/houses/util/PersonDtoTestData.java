package ru.clevertec.houses.util;

import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.model.enums.Gender;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static ru.clevertec.houses.util.ConstantsTest.UUID_H_AVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_BVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_CVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_DVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_EVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_FVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_P_AVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_P_BVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_P_CVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_P_DVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_P_EVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_P_FVAN;

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


    public static PersonDto getAvan() {
        return PersonDto.builder()
                .name("Avan")
                .surname("Avanov")
                .sex(Gender.MALE)
                .uuid(UUID_P_AVAN)
                .houseLiveUuid(UUID_H_AVAN)
                .passportNumber("1234567")
                .passportSeries("QK")
                .build();
    }
    public static PersonDto getBvan() {
        return PersonDto.builder()
                .name("Bvan")
                .surname("Bvanov")
                .sex(Gender.MALE)
                .uuid(UUID_P_BVAN)
                .houseLiveUuid(UUID_H_BVAN)
                .passportNumber("2345678")
                .passportSeries("QK")
                .build();
    }
    public static PersonDto getCvan() {
        return PersonDto.builder()
                .name("Cvan")
                .surname("Cvanov")
                .sex(Gender.MALE)
                .uuid(UUID_P_CVAN)
                .houseLiveUuid(UUID_H_CVAN)
                .passportNumber("3456789")
                .passportSeries("QK")
                .build();
    }
    public static PersonDto getDvan() {
        return PersonDto.builder()
                .name("Dvan")
                .surname("Dvanov")
                .sex(Gender.MALE)
                .uuid(UUID_P_DVAN)
                .houseLiveUuid(UUID_H_DVAN)
                .passportNumber("4567890")
                .passportSeries("QK")
                .build();
    }
    public static PersonDto getEva() {
        return PersonDto.builder()
                .name("Eva")
                .surname("Evanova")
                .sex(Gender.FEMALE)
                .uuid(UUID_P_EVAN)
                .houseLiveUuid(UUID_H_EVAN)
                .passportNumber("5678901")
                .passportSeries("QK")
                .build();
    }
    public static PersonDto getFvan() {
        return PersonDto.builder()
                .name("Fvan")
                .surname("Fvanov")
                .sex(Gender.MALE)
                .uuid(UUID_P_FVAN)
                .houseLiveUuid(UUID_H_FVAN)
                .passportNumber("6789012")
                .passportSeries("QK")
                .build();
    }

    public static List<PersonDto> getListPersonDto() {
        return Arrays.asList(getAvan(), getBvan(), getCvan(), getDvan(), getEva(), getFvan());
    }
}
