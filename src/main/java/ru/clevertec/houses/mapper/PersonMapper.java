package ru.clevertec.houses.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.PersonsHouseDto;
import ru.clevertec.houses.model.Person;

import java.util.List;

@Mapper
public interface PersonMapper {

    @Mapping(target = "houseLiveUuid", source = "residentOf.uuid")
    PersonDto personToPersonDto(Person person);

    List<PersonDto> personListToPersonDtoList(List<Person> personList);

    @Mapping(target = "residentOf.uuid", source = "houseLiveUuid")
    Person toPerson(PersonDto personDto);

    @Mapping(target = "ownHouses", source = "houses")
    PersonsHouseDto toPersonsHouseDto(Person person);

}
