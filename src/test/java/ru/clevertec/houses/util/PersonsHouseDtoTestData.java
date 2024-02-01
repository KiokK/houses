package ru.clevertec.houses.util;

import ru.clevertec.houses.dto.PersonsHouseDto;

import java.util.List;

import static ru.clevertec.houses.util.ConstantsTest.UUID_P_AVAN;
import static ru.clevertec.houses.util.HouseDtoTestData.getAvanHouseDto;

public class PersonsHouseDtoTestData {

    public static PersonsHouseDto getPersonsHouseDto() {
        PersonsHouseDto personsHouseDto = new PersonsHouseDto();
        personsHouseDto.uuid = UUID_P_AVAN;
        personsHouseDto.ownHouses = List.of(getAvanHouseDto());

        return personsHouseDto;
    }
}
