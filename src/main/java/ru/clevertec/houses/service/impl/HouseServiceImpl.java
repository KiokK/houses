package ru.clevertec.houses.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.clevertec.houses.dao.HouseDao;
import ru.clevertec.houses.dao.model.PaginationInfo;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.HouseResidentsDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.exception.EntityNotFoundException;
import ru.clevertec.houses.mapper.HouseMapper;
import ru.clevertec.houses.model.House;
import ru.clevertec.houses.service.HouseService;

import java.time.LocalDateTime;
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
    public HouseDto findHouseByUuid(UUID uuid) throws EntityNotFoundException {
        return houseDao.findHouseByUuid(uuid)
                .map(houseMapper::toHouseDto)
                .orElseThrow(() -> new EntityNotFoundException(uuid));
    }

    @Override
    public PaginationResponseDto findAll(PaginationInfo paginationInfo) {
        PaginationResponseDto responseDto = new PaginationResponseDto();

        responseDto.data = houseDao.findAll(paginationInfo).stream()
                .map(houseMapper::toHouseDto)
                .toList();
        responseDto.pageNumber = paginationInfo.getPageNumber();
        responseDto.pageSize = paginationInfo.getPageSize();

        return responseDto;
    }

    @Override
    public HouseResidentsDto findAllResidentsByHouseUuid(UUID uuid) throws EntityNotFoundException {
        return houseDao.findWithResidentsByUuid(uuid)
                .map(houseMapper::toHouseResidentsDto)
                .orElseThrow(() -> new EntityNotFoundException(uuid));
    }

    @Override
    public HouseDto update(UUID uuid, HouseDto houseDto) throws EntityNotFoundException {
        houseDto.uuid = uuid;
        House house = houseMapper.toHouse(houseDto);

        return houseDao.update(house)
                .map(houseMapper::toHouseDto)
                .orElseThrow(() -> new EntityNotFoundException(houseDto.uuid));
    }

    @Override
    public boolean deleteByUuid(UUID uuid) {
        return houseDao.deleteByUuid(uuid);
    }

}
