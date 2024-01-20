package ru.clevertec.houses.service;

import ru.clevertec.houses.dao.model.PaginationInfo;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.PersonsHouseDto;
import ru.clevertec.houses.dto.request.PersonsHouseRequestDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.exception.EntityNotFoundException;

import java.util.UUID;

public interface PersonService {

    PersonDto create(PersonDto personDto);

    PersonDto findPersonByUuid(UUID uuid) throws EntityNotFoundException;

    PaginationResponseDto findAll(PaginationInfo paginationInfo);

    PersonsHouseDto findAllOwnHousesByPersonUuid(UUID uuid) throws EntityNotFoundException;

    boolean update(UUID uuid, PersonDto personDto) throws EntityNotFoundException;

    boolean update(UUID uuid, PersonsHouseRequestDto personDto) throws EntityNotFoundException;

    boolean deleteByUuid(UUID uuid);

}
