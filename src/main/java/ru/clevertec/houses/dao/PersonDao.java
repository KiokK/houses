package ru.clevertec.houses.dao;


import ru.clevertec.houses.dao.model.PaginationInfo;
import ru.clevertec.houses.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonDao {

    Person create(Person person);

    Optional<Person> findPersonByUuid(UUID uuid);

    List<Person> findAll(PaginationInfo paginationInfo);

    Optional<Person> findWithHousesByPersonUuid(UUID uuid);

    boolean update(Person person);

    boolean updateWithOwnHouses(Person person);

    boolean deleteByUuid(UUID uuid);

}
