package ru.clevertec.houses.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.clevertec.houses.dao.HouseDao;
import ru.clevertec.houses.dao.model.PaginationInfo;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.HouseResidentsDto;
import ru.clevertec.houses.mapper.HouseMapper;
import ru.clevertec.houses.model.House;
import ru.clevertec.houses.service.HouseService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseDao houseDao;

    private final HouseMapper houseMapper = Mappers.getMapper(HouseMapper.class);

    @Override
    public HouseDto create(HouseDto houseDto) {
        House house = houseMapper.toHouse(houseDto);
        house.setUuid(UUID.randomUUID());
        house.setCreateDate(LocalDateTime.now());
        house = houseDao.create(house);

        return houseMapper.toHouseDto(house);
    }

    @Override
    public Optional<HouseDto> findHouseByUuid(UUID uuid) {
        return houseDao.findHouseByUuid(uuid)
                .map(houseMapper::toHouseDto);
    }

    @Override
    public List<HouseDto> findAll(PaginationInfo paginationInfo) {
        return houseDao.findAll(paginationInfo).stream()
                .map(houseMapper::toHouseDto)
                .toList();
    }

    @Override
    public Optional<HouseResidentsDto> findAllResidentsByHouseUuid(UUID uuid) {
        return houseDao.findWithResidentsByUuid(uuid)
                .map(houseMapper::toHouseResidentsDto)
                .or(Optional::empty);
    }

    @Override
    public Optional<HouseDto> update(HouseDto houseDto) {
        House house = houseMapper.toHouse(houseDto);
        return houseDao.update(house)
                .map(houseMapper::toHouseDto)
                .or(Optional::empty);
    }

    @Override
    public boolean deleteByUuid(UUID uuid) {
        return houseDao.deleteByUuid(uuid);
    }

}
