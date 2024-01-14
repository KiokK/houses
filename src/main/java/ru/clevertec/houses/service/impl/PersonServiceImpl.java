package ru.clevertec.houses.service.impl;

import ru.clevertec.houses.dao.PersonDao;
import ru.clevertec.houses.dao.model.PaginationInfo;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.PersonsHouseDto;
import ru.clevertec.houses.dto.request.PersonsHouseRequestDto;
import ru.clevertec.houses.mapper.HouseMapper;
import ru.clevertec.houses.mapper.PersonMapper;
import ru.clevertec.houses.model.Person;
import ru.clevertec.houses.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonDao personDao;

    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);
    private final HouseMapper houseMapper = Mappers.getMapper(HouseMapper.class);

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
    public Optional<PersonDto> findPersonByUuid(UUID uuid) {
        return personDao.findPersonByUuid(uuid)
                .map(personMapper::toPersonDto);
    }

    @Override
    public List<PersonDto> findAll(PaginationInfo paginationInfo) {
        return personDao.findAll(paginationInfo).stream()
                .map(personMapper::toPersonDto)
                .toList();
    }

    @Override
    public Optional<PersonsHouseDto> findAllOwnHousesByPersonUuid(UUID uuid) {
        return personDao.findWithHousesByPersonUuid(uuid)
                .map(personMapper::toPersonsHouseDto)
                .or(Optional::empty);
    }

    @Override
    public boolean update(PersonDto personDto) {
        Person person = personMapper.toPerson(personDto);
        person.setUpdateDate(LocalDateTime.now());
        return personDao.update(person);
    }

    @Override
    public boolean update(PersonsHouseRequestDto personDto) {
        Person person = personMapper.toPerson(personDto);
        person.setUpdateDate(LocalDateTime.now());
        return personDao.updateWithOwnHouses(person);
    }

    @Override
    public boolean deleteByUuid(UUID uuid) {
        return personDao.deleteByUuid(uuid);
    }

}
