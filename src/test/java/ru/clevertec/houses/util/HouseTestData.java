package ru.clevertec.houses.util;

import org.mapstruct.factory.Mappers;
import ru.clevertec.houses.mapper.HouseMapper;
import ru.clevertec.houses.model.House;
import ru.clevertec.houses.model.Person;

import java.util.List;

import static ru.clevertec.houses.util.ConstantsTest.DEFAULT_CREATED_TIME;
import static ru.clevertec.houses.util.ConstantsTest.DEFAULT_ID_1;
import static ru.clevertec.houses.util.ConstantsTest.DEFAULT_ID_2;
import static ru.clevertec.houses.util.ConstantsTest.DEFAULT_ID_3;
import static ru.clevertec.houses.util.HouseDtoTestData.getAvanHouseDto;
import static ru.clevertec.houses.util.HouseDtoTestData.getBvanHouseDto;
import static ru.clevertec.houses.util.PersonTestData.getPersonAvan;
import static ru.clevertec.houses.util.PersonTestData.getPersonBvan;

public class HouseTestData {

    private static final HouseMapper houseMapper = Mappers.getMapper(HouseMapper.class);

    public static House getAvanHouse() {
        House house = houseMapper.houseDtoToHouse(getAvanHouseDto());
        house.setId(DEFAULT_ID_1);
        house.setCreateDate(DEFAULT_CREATED_TIME);

        return house;
    }
    public static House getBvanHouse() {
        House house = houseMapper.houseDtoToHouse(getBvanHouseDto());
        house.setId(DEFAULT_ID_2);
        house.setCreateDate(DEFAULT_CREATED_TIME);

        return house;
    }

    public static House getCvanHouse() {
        House house = houseMapper.houseDtoToHouse(getBvanHouseDto());
        house.setId(DEFAULT_ID_3);
        house.setCreateDate(DEFAULT_CREATED_TIME);

        return house;
    }

    public static House getAvanHouseWithTwoResidents() {
        House avanHouse = houseMapper.houseDtoToHouse(getAvanHouseDto());
        Person person1 = getPersonAvan();
        person1.setResidentOf(avanHouse);
        Person person2 = getPersonBvan();
        person2.setResidentOf(avanHouse);
        avanHouse.setResidents(List.of(person1, person2));

        return avanHouse;
    }

    public static List<House> getListOfTwoHouses() {
        return List.of(getAvanHouse(), getBvanHouse());
    }
}
