package ru.clevertec.houses.util;

import ru.clevertec.houses.dto.HouseDto;

import java.util.Arrays;
import java.util.List;

import static ru.clevertec.houses.util.ConstantsTest.UUID_H_AVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_BVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_CVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_DVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_EVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_FVAN;

public class HouseDtoTestData {

    public static HouseDto.HouseDtoBuilder validHouseDtoBuilder() {
        return HouseDto.builder()
                .area(100.0F)
                .city("Minsk")
                .country("Belarus")
                .street("Nemiga")
                .number(2);
    }

    public static HouseDto getAvanHouse() {
        return HouseDto.builder()
                .uuid(UUID_H_AVAN)
                .area(100.0F)
                .city("Minsk")
                .country("Belarus")
                .street("Nemiga")
                .number(20)
                .build();
    }

    public static HouseDto getBvanHouse() {
        return HouseDto.builder()
                .uuid(UUID_H_BVAN)
                .area(104.0F)
                .city("Minsk")
                .country("Belarus")
                .street("Nemiga")
                .number(212)
                .build();
    }

    public static HouseDto getCvanHouse() {
        return HouseDto.builder()
                .uuid(UUID_H_CVAN)
                .area(110.0F)
                .city("Minsk")
                .country("Belarus")
                .street("Nemiga")
                .number(232)
                .build();
    }

    public static HouseDto getDvanHouse() {
        return HouseDto.builder()
                .uuid(UUID_H_DVAN)
                .area(130.0F)
                .city("Minsk")
                .country("Belarus")
                .street("Nemiga")
                .number(22)
                .build();
    }

    public static HouseDto getEvanHouse() {
        return HouseDto.builder()
                .uuid(UUID_H_EVAN)
                .area(50.0F)
                .city("Minsk")
                .country("Belarus")
                .street("Nemiga")
                .number(12)
                .build();
    }

    public static HouseDto getFvanHouse() {
        return HouseDto.builder()
                .uuid(UUID_H_FVAN)
                .area(100.0F)
                .city("Minsk")
                .country("Belarus")
                .street("Surganova")
                .number(2)
                .build();
    }

    public static List<HouseDto> getListHouseDto() {
        return Arrays.asList(getAvanHouse(), getBvanHouse(), getCvanHouse(), getDvanHouse(), getEvanHouse(), getFvanHouse());
    }

}
