package ru.clevertec.houses.util.dto;

import ru.clevertec.houses.dto.HouseResidentsDto;
import ru.clevertec.houses.dto.PersonDto;

import java.util.List;

import static ru.clevertec.houses.util.dto.HouseDtoTestData.getAvanHouse;
import static ru.clevertec.houses.util.dto.PersonDtoTestData.getAvan;
import static ru.clevertec.houses.util.dto.PersonDtoTestData.getBvan;

public class HouseResidentsDtoTestData {

    public static HouseResidentsDto getAvanHouseResidentsDto() {
        HouseResidentsDto houseResidentsDto = new HouseResidentsDto();
        houseResidentsDto.uuid = getAvanHouse().uuid;
        PersonDto avanNeighbour = getBvan();
        avanNeighbour.houseLiveUuid = houseResidentsDto.uuid;
        houseResidentsDto.residents = List.of(getAvan(), getBvan());

        return houseResidentsDto;
    }
}
