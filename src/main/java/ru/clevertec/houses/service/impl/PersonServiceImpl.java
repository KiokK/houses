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
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.PersonsHouseDto;
import ru.clevertec.houses.dto.request.PersonsHouseRequestDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.dto.response.PersonHistoryDto;
import ru.clevertec.houses.exception.EntityNotFoundException;
import ru.clevertec.houses.mapper.HouseMapper;
import ru.clevertec.houses.mapper.PersonMapper;
import ru.clevertec.houses.model.House;
import ru.clevertec.houses.model.Person;
import ru.clevertec.houses.model.enums.HistoryType;
import ru.clevertec.houses.service.PersonService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonServiceImpl implements PersonService {

    private final PersonDao personDao;
    private final HouseDao houseDao;
    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);
    private final HouseMapper houseMapper = Mappers.getMapper(HouseMapper.class);

    @Override
    @PutToCache
    @Transactional
    public PersonDto create(PersonDto personDto) throws EntityNotFoundException {
        Person person = personMapper.toPerson(personDto);
        person.setUuid(UUID.randomUUID());
        LocalDateTime creationTime = LocalDateTime.now();
        person.setCreateDate(creationTime);
        person.setUpdateDate(creationTime);

        UUID newHouseUuid = person.getResidentOf().getUuid();
        House newPersonHouse = houseDao.findByUuid(newHouseUuid)
                .orElseThrow(() -> new EntityNotFoundException(newHouseUuid));
        person.setResidentOf(newPersonHouse);

        return personMapper.personToPersonDto(personDao.save(person));
    }

    @Override
    @GetFromCache
    public PersonDto findPersonByUuid(UUID uuid) throws EntityNotFoundException {
        return personDao.findByUuid(uuid)
                .map(personMapper::personToPersonDto)
                .orElseThrow(() -> new EntityNotFoundException(uuid));
    }

    @Override
    public PaginationResponseDto findAll(Pageable pageable) {
        PaginationResponseDto responseDto = new PaginationResponseDto();

        responseDto.data = personDao.findAll(pageable)
                .map(personMapper::personToPersonDto)
                .toList();
        responseDto.pageNumber = pageable.getPageNumber();
        responseDto.pageSize = pageable.getPageSize();

        return responseDto;
    }

    @Override
    public PersonsHouseDto findAllOwnHousesByPersonUuid(UUID uuid) throws EntityNotFoundException {
        return personDao.findWithHousesByPersonUuid(uuid)
                .map(personMapper::toPersonsHouseDto)
                .orElseThrow(() -> new EntityNotFoundException(uuid));
    }

    @Override
    public PersonHistoryDto findHousesByPersonUuidAndHistoryType(UUID personUuid, Pageable pageable, HistoryType historyType) throws EntityNotFoundException {
        PersonHistoryDto historyResponse = new PersonHistoryDto();
        historyResponse.personUuid = personUuid;
        historyResponse.pageNumber = pageable.getPageNumber();
        historyResponse.pageSize = pageable.getPageSize();
        List<House> foundHouses = houseDao.findAllByHouseHistory_person_uuidAndHouseHistory_type(personUuid, historyType, pageable);
        historyResponse.houseDtoList = houseMapper.houseListToHouseDtoList(foundHouses);

        return historyResponse;
    }

    @Override
    @PostFromCache
    @Transactional
    public PersonDto update(UUID uuid, PersonDto personDto) throws EntityNotFoundException {
        personDto.uuid = uuid;
        Person person = personMapper.toPerson(personDto);
        person.setUpdateDate(LocalDateTime.now());

        Long foundId = personDao.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(uuid))
                .getId();
        person.setId(foundId);
        UUID newHouseUuid = person.getResidentOf().getUuid();
        House newPersonHouse = houseDao.findByUuid(newHouseUuid)
                .orElseThrow(() -> new EntityNotFoundException(newHouseUuid));
        person.setResidentOf(newPersonHouse);
        person = personDao.save(person);

        return personMapper.personToPersonDto(person);
    }

    @Override
    @Transactional
    public boolean update(UUID uuid, PersonsHouseRequestDto personDto) throws EntityNotFoundException {
        Person person = personDao.findByUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException(uuid));
        person.setUpdateDate(LocalDateTime.now());

        List<House> newHouses = houseDao.findAllByUuidIn(personDto.houseUuids);
        person.setHouses(newHouses);
        personDao.save(person);

        return true;
    }

    /**
     * Удаляет person без связных объектов
     * @param uuid person
     * @return true при успешном удалении, иначе - false
     */
    @Override
    @Transactional
    @DeleteFromCache
    public boolean deleteByUuid(UUID uuid) {
        return personDao.deletePersonByUuid(uuid) == 1;
    }

}
