package ru.clevertec.houses.service;

import ru.clevertec.houses.dao.model.PaginationInfo;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.HouseResidentsDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HouseService {

    HouseDto create(HouseDto houseDto);

    Optional<HouseDto> findHouseByUuid(UUID uuid);

    List<HouseDto> findAll(PaginationInfo paginationInfo);

    Optional<HouseResidentsDto> findAllResidentsByHouseUuid(UUID uuid);

    Optional<HouseDto> update(HouseDto houseDto);

    boolean deleteByUuid(UUID uuid);

}
