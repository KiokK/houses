package ru.clevertec.houses.util;

import org.mapstruct.factory.Mappers;
import ru.clevertec.houses.mapper.PersonMapper;
import ru.clevertec.houses.model.Person;

import java.util.List;

import static ru.clevertec.houses.util.ConstantsTest.DEFAULT_CREATED_TIME;
import static ru.clevertec.houses.util.ConstantsTest.DEFAULT_ID_1;
import static ru.clevertec.houses.util.ConstantsTest.DEFAULT_ID_2;
import static ru.clevertec.houses.util.HouseTestData.getAvanHouse;
import static ru.clevertec.houses.util.HouseTestData.getBvanHouse;
import static ru.clevertec.houses.util.PersonDtoTestData.getAvan;
import static ru.clevertec.houses.util.PersonDtoTestData.getBvan;

public class PersonTestData {

    private static final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);

    public static Person getPersonAvan() {
        Person personAvan = personMapper.toPerson(getAvan());
        personAvan.setId(DEFAULT_ID_1);
        personAvan.setResidentOf(getAvanHouse());
        personAvan.setCreateDate(DEFAULT_CREATED_TIME);
        personAvan.setUpdateDate(DEFAULT_CREATED_TIME);

        return personAvan;
    }

    public static Person getPersonBvan() {
        Person personBvan = personMapper.toPerson(getBvan());
        personBvan.setId(DEFAULT_ID_2);
        personBvan.setResidentOf(getBvanHouse());
        personBvan.setCreateDate(DEFAULT_CREATED_TIME);
        personBvan.setUpdateDate(DEFAULT_CREATED_TIME);

        return personBvan;
    }

    public static List<Person> getListPersonSize2() {
        return List.of(getPersonAvan(), getPersonBvan());
    }
}
