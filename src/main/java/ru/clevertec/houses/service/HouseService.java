package ru.clevertec.houses.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.HouseResidentsDto;
import ru.clevertec.houses.dto.response.HouseHistoryDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException;
import ru.clevertec.houses.model.enums.HistoryType;

import java.util.UUID;

public interface HouseService {

    HouseDto create(HouseDto houseDto);

    HouseDto findHouseByUuid(UUID uuid) throws EntityNotFoundException;

    PaginationResponseDto findAll(Pageable pageable);

    HouseResidentsDto findAllResidentsByHouseUuid(UUID uuid) throws EntityNotFoundException;

    HouseHistoryDto findPersonsByHouseUuidAndHistoryType(UUID houseUuid, Pageable pageable, HistoryType historyType) throws EntityNotFoundException;

    HouseDto update(UUID uuid, HouseDto houseDto) throws EntityNotFoundException;

    boolean deleteByUuid(UUID uuid);

}
