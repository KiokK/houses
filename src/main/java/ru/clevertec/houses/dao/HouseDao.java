package ru.clevertec.houses.dao;

import ru.clevertec.houses.dao.model.PaginationInfo;
import ru.clevertec.houses.model.House;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HouseDao {

    House create(House house);

    Optional<House> findHouseByUuid(UUID uuid);

    List<House> findAll(PaginationInfo paginationInfo);

    Optional<House> findWithResidentsByUuid(UUID uuid);

    Optional<House> update(House house);

    boolean deleteByUuid(UUID uuid);

}
