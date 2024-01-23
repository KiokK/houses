package ru.clevertec.houses.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.PersonsHouseDto;
import ru.clevertec.houses.dto.request.PersonsHouseRequestDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.dto.response.PersonHistoryDto;
import ru.clevertec.houses.exception.EntityNotFoundException;
import ru.clevertec.houses.model.enums.HistoryType;

import java.util.UUID;

public interface PersonService {

    PersonDto create(PersonDto personDto) throws EntityNotFoundException;

    PersonDto findPersonByUuid(UUID uuid) throws EntityNotFoundException;

    PaginationResponseDto findAll(Pageable pageable);

    PersonsHouseDto findAllOwnHousesByPersonUuid(UUID uuid) throws EntityNotFoundException;

    PersonHistoryDto findHousesByPersonUuidAndHistoryType(UUID personUuid, Pageable pageable, HistoryType historyType) throws EntityNotFoundException;

    boolean update(UUID uuid, PersonDto personDto) throws EntityNotFoundException;

    boolean update(UUID uuid, PersonsHouseRequestDto personDto) throws EntityNotFoundException;

    boolean deleteByUuid(UUID uuid);

}
