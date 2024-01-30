package ru.clevertec.houses.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.cachestarter.cache.proxy.DeleteFromCache;
import ru.clevertec.cachestarter.cache.proxy.GetFromCache;
import ru.clevertec.cachestarter.cache.proxy.PostFromCache;
import ru.clevertec.cachestarter.cache.proxy.PutToCache;
import ru.clevertec.houses.dao.HouseDao;
import ru.clevertec.houses.dao.PersonDao;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.HouseResidentsDto;
import ru.clevertec.houses.dto.response.HouseHistoryDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.exception.EntityNotFoundException;
import ru.clevertec.houses.mapper.HouseMapper;
import ru.clevertec.houses.mapper.PersonMapper;
import ru.clevertec.houses.model.House;
import ru.clevertec.houses.model.Person;
import ru.clevertec.houses.model.enums.HistoryType;
import ru.clevertec.houses.service.HouseService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

    private final HouseDao houseDao;
    private final PersonDao personDao;
    private final HouseMapper houseMapper = Mappers.getMapper(HouseMapper.class);
    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

    @Override
    @PutToCache
    @Transactional
    public HouseDto create(HouseDto houseDto) {
        House house = houseMapper.houseDtoToHouse(houseDto);
        house.setUuid(UUID.randomUUID());
        house.setCreateDate(LocalDateTime.now());
        house = houseDao.save(house);

        return houseMapper.toHouseDto(house);
    }

    @Override
    @GetFromCache
    public HouseDto findHouseByUuid(UUID uuid) throws EntityNotFoundException {
        return houseDao.findByUuid(uuid)
                .map(houseMapper::toHouseDto)
                .orElseThrow(() -> new EntityNotFoundException(uuid));
    }

    @Override
    public PaginationResponseDto findAll(Pageable pageable) {
        PaginationResponseDto responseDto = new PaginationResponseDto();

        responseDto.data = houseDao.findAll(pageable)
                .map(houseMapper::toHouseDto)
                .toList();
        responseDto.pageNumber = pageable.getPageNumber();
        responseDto.pageSize = pageable.getPageSize();

        return responseDto;
    }

    @Override
    public HouseHistoryDto findPersonsByHouseUuidAndHistoryType(UUID houseUuid, Pageable pageable, HistoryType historyType) throws EntityNotFoundException {
        HouseHistoryDto historyResponse = new HouseHistoryDto();
        historyResponse.houseUuid = houseUuid;
        historyResponse.pageNumber = pageable.getPageNumber();
        historyResponse.pageSize = pageable.getPageSize();
        List<Person> foundPersons = personDao.findAllPersonsByHouseUuidAndHistoryType(houseUuid, pageable, historyType);
        historyResponse.personDtoList = personMapper.personListToPersonDtoList(foundPersons);

        return historyResponse;
    }

    @Override
    public HouseResidentsDto findAllResidentsByHouseUuid(UUID uuid) throws EntityNotFoundException {
        return houseDao.findWithResidentsByUuid(uuid)
                .map(houseMapper::toHouseResidentsDto)
                .orElseThrow(() -> new EntityNotFoundException(uuid));
    }

    @Override
    @Transactional
    @PostFromCache
    public HouseDto update(UUID uuid, HouseDto houseDto) throws EntityNotFoundException {
        houseDto.uuid = uuid;
        House house = houseMapper.houseDtoToHouse(houseDto);

        Long foundId = houseDao.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(houseDto.uuid))
                .getId();
        house.setId(foundId);
        house = houseDao.save(house);

        return houseMapper.toHouseDto(house);
    }

    @Override
    @Transactional
    @DeleteFromCache
    public boolean deleteByUuid(UUID uuid) {
        return houseDao.deleteByUuid(uuid) == 1;
    }

}
