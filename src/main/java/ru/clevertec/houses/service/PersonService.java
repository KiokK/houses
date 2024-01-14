package ru.clevertec.houses.service;

import ru.clevertec.houses.dao.model.PaginationInfo;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.PersonsHouseDto;
import ru.clevertec.houses.dto.request.PersonsHouseRequestDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonService {

    PersonDto create(PersonDto personDto);

    Optional<PersonDto> findPersonByUuid(UUID uuid);

    List<PersonDto> findAll(PaginationInfo paginationInfo);

    Optional<PersonsHouseDto> findAllOwnHousesByPersonUuid(UUID uuid);

    boolean update(PersonDto personDto);

    boolean update(PersonsHouseRequestDto personDto);

    boolean deleteByUuid(UUID uuid);

}
