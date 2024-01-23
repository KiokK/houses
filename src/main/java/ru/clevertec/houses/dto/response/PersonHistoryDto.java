package ru.clevertec.houses.dto.response;

import ru.clevertec.houses.dto.HouseDto;

import java.util.List;
import java.util.UUID;

public class PersonHistoryDto {

    public UUID personUuid;
    public int pageSize;
    public int pageNumber;
    public List<HouseDto> houseDtoList;

}
