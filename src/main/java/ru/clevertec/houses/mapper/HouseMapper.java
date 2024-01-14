package ru.clevertec.houses.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.HouseResidentsDto;
import ru.clevertec.houses.model.House;

@Mapper
public interface HouseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createDate", ignore = true)
    House toHouse(HouseDto requestHouseDto);

    HouseDto toHouseDto(House house);

    HouseResidentsDto toHouseResidentsDto(House house);

}
