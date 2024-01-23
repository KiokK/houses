package ru.clevertec.houses.dto.response;

import ru.clevertec.houses.dto.PersonDto;

import java.util.List;
import java.util.UUID;

public class HouseHistoryDto {

    public UUID houseUuid;
    public int pageSize;
    public int pageNumber;
    public List<PersonDto> personDtoList;

}
