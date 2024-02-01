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

    public static HouseDto getAvanHouseDto() {
        return HouseDto.builder()
                .uuid(UUID_H_AVAN)
                .area(100.0F)
                .city("Minsk")
                .country("Belarus")
                .street("Nemiga")
                .number(20)
                .build();
    }

    public static HouseDto getBvanHouseDto() {
        return HouseDto.builder()
                .uuid(UUID_H_BVAN)
                .area(104.0F)
                .city("Minsk")
                .country("Belarus")
                .street("Nemiga")
                .number(212)
                .build();
    }

    public static HouseDto getCvanHouseDto() {
        return HouseDto.builder()
                .uuid(UUID_H_CVAN)
                .area(110.0F)
                .city("Minsk")
                .country("Belarus")
                .street("Nemiga")
                .number(232)
                .build();
    }

    public static HouseDto getDvanHouseDto() {
        return HouseDto.builder()
                .uuid(UUID_H_DVAN)
                .area(130.0F)
                .city("Minsk")
                .country("Belarus")
                .street("Nemiga")
                .number(22)
                .build();
    }

    public static HouseDto getEvanHouseDto() {
        return HouseDto.builder()
                .uuid(UUID_H_EVAN)
                .area(50.0F)
                .city("Minsk")
                .country("Belarus")
                .street("Nemiga")
                .number(12)
                .build();
    }

    public static HouseDto getFvanHouseDto() {
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
        return Arrays.asList(getAvanHouseDto(), getBvanHouseDto(), getCvanHouseDto(), getDvanHouseDto(), getEvanHouseDto(), getFvanHouseDto());
    }

    public static List<HouseDto> getListOfTwoHouseDto() {
        return Arrays.asList(getAvanHouseDto(), getBvanHouseDto());
    }

}
