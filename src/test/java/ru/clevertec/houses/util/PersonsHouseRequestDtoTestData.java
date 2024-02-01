package ru.clevertec.houses.util;

import ru.clevertec.houses.dto.request.PersonsHouseRequestDto;

import java.util.List;

import static ru.clevertec.houses.util.ConstantsTest.UUID_H_AVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_BVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_DVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_P_AVAN;

public class PersonsHouseRequestDtoTestData {

    public static PersonsHouseRequestDto getPersonsHouseRequestDtoWith3Houses() {
        PersonsHouseRequestDto requestDto = new PersonsHouseRequestDto();
        requestDto.personUuid = UUID_P_AVAN;
        requestDto.houseUuids = List.of(UUID_H_AVAN, UUID_H_BVAN, UUID_H_DVAN);

        return requestDto;
    }
}
