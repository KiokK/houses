package ru.clevertec.houses.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.HouseResidentsDto;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.model.House;
import ru.clevertec.houses.model.Person;

import java.util.List;

@Mapper
public interface HouseMapper {

    House houseDtoToHouse(HouseDto houseDto);

    HouseDto toHouseDto(House house);

    List<HouseDto> houseListToHouseDtoList(List<House> houseList);

    HouseResidentsDto toHouseResidentsDto(House house);

    @Mapping(target = "houseLiveUuid", source = "residentOf.uuid")
    PersonDto personToPersonDto(Person person);

}
