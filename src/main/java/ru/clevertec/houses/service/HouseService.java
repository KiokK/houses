package ru.clevertec.houses.service;

import ru.clevertec.houses.dao.model.PaginationInfo;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.HouseResidentsDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.exception.EntityNotFoundException;

import java.util.UUID;

public interface HouseService {

    HouseDto create(HouseDto houseDto);

    HouseDto findHouseByUuid(UUID uuid) throws EntityNotFoundException;

    PaginationResponseDto findAll(PaginationInfo paginationInfo);

    HouseResidentsDto findAllResidentsByHouseUuid(UUID uuid) throws EntityNotFoundException;

    HouseDto update(UUID uuid, HouseDto houseDto) throws EntityNotFoundException;

    boolean deleteByUuid(UUID uuid);

}
