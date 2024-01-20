package ru.clevertec.houses.service.impl;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.clevertec.houses.dao.PersonDao;
import ru.clevertec.houses.dao.model.PaginationInfo;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.PersonsHouseDto;
import ru.clevertec.houses.dto.request.PersonsHouseRequestDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.exception.EntityNotFoundException;
import ru.clevertec.houses.mapper.PersonMapper;
import ru.clevertec.houses.model.Person;
import ru.clevertec.houses.service.PersonService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonDao personDao;
    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

    @Override
    public PersonDto create(PersonDto personDto) {
        Person person = personMapper.toPerson(personDto);
        person.setUuid(UUID.randomUUID());
        LocalDateTime creationTime = LocalDateTime.now();
        person.setCreateDate(creationTime);
        person.setUpdateDate(creationTime);

        return personMapper.toPersonDto(personDao.create(person));
    }

    @Override
    public PersonDto findPersonByUuid(UUID uuid) throws EntityNotFoundException {
        return personDao.findPersonByUuid(uuid)
                .map(personMapper::toPersonDto)
                .orElseThrow(() -> new EntityNotFoundException(uuid));
    }

    @Override
    public PaginationResponseDto findAll(PaginationInfo paginationInfo) {
        PaginationResponseDto responseDto = new PaginationResponseDto();

        responseDto.data = personDao.findAll(paginationInfo).stream()
                .map(personMapper::toPersonDto)
                .toList();
        responseDto.pageNumber = paginationInfo.getPageNumber();
        responseDto.pageSize = paginationInfo.getPageSize();

        return responseDto;
    }

    @Override
    public PersonsHouseDto findAllOwnHousesByPersonUuid(UUID uuid) throws EntityNotFoundException {
        return personDao.findWithHousesByPersonUuid(uuid)
                .map(personMapper::toPersonsHouseDto)
                .orElseThrow(() -> new EntityNotFoundException(uuid));
    }

    @Override
    public boolean update(UUID uuid, PersonDto personDto) throws EntityNotFoundException {
        personDto.uuid = uuid;
        Person person = personMapper.toPerson(personDto);
        person.setUpdateDate(LocalDateTime.now());

        return personDao.update(person);
    }

    @Override
    public boolean update(UUID uuid, PersonsHouseRequestDto personDto) throws EntityNotFoundException {
        personDto.personUuid = uuid;
        Person person = personMapper.toPerson(personDto);
        person.setUpdateDate(LocalDateTime.now());

        return personDao.updateWithOwnHouses(person);
    }

    @Override
    public boolean deleteByUuid(UUID uuid) {
        return personDao.deleteByUuid(uuid);
    }

}
