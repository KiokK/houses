package ru.clevertec.houses.util;

import ru.clevertec.houses.dto.HouseDto;

public class HouseDtoTestData {

    public static HouseDto.HouseDtoBuilder validHouseDtoBuilder() {
        return HouseDto.builder()
                .area(100.0F)
                .city("Minsk")
                .country("Belarus")
                .street("Nemiga")
                .number(2);
    }

}
