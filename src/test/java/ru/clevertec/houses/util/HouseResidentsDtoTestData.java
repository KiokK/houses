package ru.clevertec.houses.util;

import ru.clevertec.houses.dto.HouseResidentsDto;
import ru.clevertec.houses.dto.PersonDto;

import java.util.List;

import static ru.clevertec.houses.util.ConstantsTest.UUID_H_AVAN;
import static ru.clevertec.houses.util.PersonDtoTestData.getAvan;
import static ru.clevertec.houses.util.PersonDtoTestData.getBvan;

public class HouseResidentsDtoTestData {

    public static HouseResidentsDto getAvanHouseResidentsDto() {
        HouseResidentsDto houseResidentsDto = new HouseResidentsDto();
        houseResidentsDto.uuid = UUID_H_AVAN;
        PersonDto avanNeighbour = getBvan();
        avanNeighbour.houseLiveUuid = houseResidentsDto.uuid;
        houseResidentsDto.residents = List.of(getAvan(), avanNeighbour);

        return houseResidentsDto;
    }
}
