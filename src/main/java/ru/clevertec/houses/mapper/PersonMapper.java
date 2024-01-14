package ru.clevertec.houses.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.PersonsHouseDto;
import ru.clevertec.houses.dto.request.PersonsHouseRequestDto;
import ru.clevertec.houses.model.Person;

@Mapper
public interface PersonMapper {

    @Mapping(target = "houseLiveUuid", source = "residentOf.uuid")
    PersonDto toPersonDto(Person person);

    @Mapping(target = "residentOf.uuid", source = "houseLiveUuid")
    Person toPerson(PersonDto personDto);

    @Mapping(target = "uuid", source = "personUuid")
    Person toPerson(PersonsHouseRequestDto personsHouseRequestDto);

    @Mapping(target = "ownHouses", source = "houses")
    PersonsHouseDto toPersonsHouseDto(Person person);

}
