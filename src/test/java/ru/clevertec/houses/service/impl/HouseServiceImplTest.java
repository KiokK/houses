package ru.clevertec.houses.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.houses.dao.HouseDao;
import ru.clevertec.houses.dao.PersonDao;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.HouseResidentsDto;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.response.HouseHistoryDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.exception.EntityNotFoundException;
import ru.clevertec.houses.model.House;
import ru.clevertec.houses.model.Person;
import ru.clevertec.houses.model.enums.HistoryType;
import ru.clevertec.houses.util.HouseTestData;
import ru.clevertec.houses.util.PaginationResponseDtoTestData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.houses.util.ConstantsTest.NEW_AREA;
import static ru.clevertec.houses.util.ConstantsTest.NEW_COUNTRY;
import static ru.clevertec.houses.util.ConstantsTest.NEW_STREET;
import static ru.clevertec.houses.util.ConstantsTest.NOT_EXISTS_UUID;
import static ru.clevertec.houses.util.ConstantsTest.PAGE_NUMBER_0;
import static ru.clevertec.houses.util.ConstantsTest.PAGE_SIZE_6;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_AVAN;
import static ru.clevertec.houses.util.HouseDtoTestData.getAvanHouseDto;
import static ru.clevertec.houses.util.HouseDtoTestData.getBvanHouseDto;
import static ru.clevertec.houses.util.HouseHistoryDtoTestData.createHouseHistoryDto;
import static ru.clevertec.houses.util.HouseResidentsDtoTestData.getAvanHouseResidentsDto;
import static ru.clevertec.houses.util.HouseTestData.getAvanHouse;
import static ru.clevertec.houses.util.HouseTestData.getAvanHouseWithTwoResidents;
import static ru.clevertec.houses.util.HouseTestData.getBvanHouse;
import static ru.clevertec.houses.util.PersonDtoTestData.getListPersonDtoSize2;
import static ru.clevertec.houses.util.PersonTestData.getListPersonSize2;

@ExtendWith(MockitoExtension.class)
class HouseServiceImplTest {

    @InjectMocks
    private HouseServiceImpl houseService;

    @Mock
    private HouseDao houseDao;

    @Mock
    private PersonDao personDao;

    @Nested
    class Create {

        @Test
        void checkCreateShouldReturnCreatedHouseDto() {
            //given
            HouseDto expectedHouseDto = getAvanHouseDto();
            House testHouse = getAvanHouse();

            //when
            when(houseDao.save(any(House.class)))
                    .thenReturn(testHouse);
            HouseDto actualHouseDto = houseService.create(expectedHouseDto);

            //then
            assertAll(
                    () -> assertNotNull(expectedHouseDto.uuid),
                    () -> assertEquals(expectedHouseDto.area, actualHouseDto.area),
                    () -> assertEquals(expectedHouseDto.number, actualHouseDto.number),
                    () -> assertEquals(expectedHouseDto.city, actualHouseDto.city),
                    () -> assertEquals(expectedHouseDto.country, actualHouseDto.country),
                    () -> assertEquals(expectedHouseDto.street, actualHouseDto.street)
            );
        }
    }

    @Nested
    class FindHouseByUuid {

        @Test
        void checkFindHouseByUuidShouldReturnFoundHouseDto() {
            //given
            UUID testUuid = UUID_H_AVAN;
            HouseDto expectedHouseDto = getAvanHouseDto();
            House testHouse = getAvanHouse();

            //when
            when(houseDao.findByUuid(testUuid))
                    .thenReturn(Optional.of(testHouse));
            HouseDto actualHouseDto = houseService.findHouseByUuid(testUuid);

            //then
            assertEquals(expectedHouseDto, actualHouseDto);
        }

        @Test
        void checkFindHouseByUuidShouldThrowEntityNotFoundException() {
            //given
            UUID testUuid = NOT_EXISTS_UUID;

            //when
            when(houseDao.findByUuid(testUuid))
                    .thenReturn(Optional.empty());

            //then
            assertThrows(EntityNotFoundException.class, () -> houseService.findHouseByUuid(testUuid));
        }

    }

    @Nested
    class FindAll {

        @Test
        void checkFindAllShouldReturnCorrectPaginationResponse() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_6);
            Page<House> houses = new PageImpl<>(List.of(getAvanHouse(), getBvanHouse()));
            List<HouseDto> dtoHouses = List.of(getAvanHouseDto(), getBvanHouseDto());
            PaginationResponseDto expectedResponseDto = PaginationResponseDtoTestData.createResponse(PAGE_SIZE_6, PAGE_NUMBER_0, dtoHouses);

            //when
            when(houseDao.findAll(pageable))
                    .thenReturn(houses);
            PaginationResponseDto actualResponseDto = houseService.findAll(pageable);

            //then
            assertAll(
                    () -> assertEquals(expectedResponseDto.pageSize, actualResponseDto.pageSize),
                    () -> assertEquals(expectedResponseDto.pageNumber, actualResponseDto.pageNumber),
                    () -> assertEquals(expectedResponseDto.data.size(), actualResponseDto.data.size()),
                    () -> assertEquals(expectedResponseDto.data.get(0), actualResponseDto.data.get(0)),
                    () -> assertEquals(expectedResponseDto.data.get(1), actualResponseDto.data.get(1))
            );

        }
    }

    @Nested
    class FindPersonsByHouseUuidAndHistoryType {

        @Test
        void checkFindPersonsByHouseUuidAndHistoryTypeShouldThrowEntityNotFoundException() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_6);
            HistoryType historyType = HistoryType.TENANT;

            //when
            when(houseDao.findByUuid(NOT_EXISTS_UUID))
                    .thenThrow(EntityNotFoundException.class);

            //then
            assertAll(
                    () -> assertThrows(EntityNotFoundException.class,
                            () -> houseService.findPersonsByHouseUuidAndHistoryType(NOT_EXISTS_UUID, pageable, historyType)),
                    () -> verify(personDao, times(0))
                            .findAllByHouseHistory_house_uuidAndHouseHistory_type(NOT_EXISTS_UUID, historyType, pageable)
            );
        }

        @Test
        void checkFindPersonsByHouseUuidAndHistoryTypeShouldReturnCorrectResponse() {
            //given
            UUID testUuid = UUID_H_AVAN;
            House testHouse = HouseTestData.getAvanHouse();
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_6);
            HistoryType historyType = HistoryType.TENANT;
            List<PersonDto> dtoPersons = getListPersonDtoSize2();
            List<Person> persons = getListPersonSize2();
            HouseHistoryDto expected = createHouseHistoryDto(testUuid, pageable, dtoPersons);

            //when
            when(houseDao.findByUuid(testUuid))
                    .thenReturn(Optional.of(testHouse));
            when(personDao.findAllByHouseHistory_house_uuidAndHouseHistory_type(testUuid, historyType, pageable))
                    .thenReturn(persons);
            HouseHistoryDto actual = houseService.findPersonsByHouseUuidAndHistoryType(testUuid, pageable, historyType);

            //then
            assertAll(
                    () -> assertEquals(expected.pageNumber, actual.pageNumber),
                    () -> assertEquals(expected.pageSize, actual.pageSize),
                    () -> assertEquals(expected.houseUuid, actual.houseUuid),
                    () -> assertEquals(expected.personDtoList.size(), actual.personDtoList.size())
            );
        }

    }

    @Nested
    class FindAllResidentsByHouseUuid {

        @Test
        void checkFindAllResidentsByHouseUuidShouldThrowEntityNotFoundException() {
            //when
            when(houseDao.findWithResidentsByUuid(NOT_EXISTS_UUID)).thenReturn(Optional.empty());

            //then
            assertThrows(EntityNotFoundException.class,
                    () -> houseService.findAllResidentsByHouseUuid(NOT_EXISTS_UUID));
        }

        @Test
        void checkFindAllResidentsByHouseUuidShouldReturnCorrectDto() {
            //given
            int expectedResidentsCount = 2;
            final UUID testHouseUuid = UUID_H_AVAN;
            HouseResidentsDto expected = getAvanHouseResidentsDto();
            House houseWithTwoResidents = getAvanHouseWithTwoResidents();

            //when
            when(houseDao.findWithResidentsByUuid(testHouseUuid))
                    .thenReturn(Optional.of(houseWithTwoResidents));
            HouseResidentsDto actual = houseService.findAllResidentsByHouseUuid(testHouseUuid);

            //then
            assertAll(
                    () -> assertEquals(expected.uuid, actual.uuid),
                    () -> assertEquals(expectedResidentsCount, actual.residents.size()),
                    () -> assertEquals(expected.residents.get(0), actual.residents.get(0)),
                    () -> assertEquals(expected.residents.get(1), actual.residents.get(1))
            );
        }

    }

    @Nested
    class Update {

        @Test
        void checkUpdateShouldReturnCreatedHouseDto() {
            //given
            UUID houseUuid = UUID_H_AVAN;
            House previousFoundHouse = getAvanHouse();
            HouseDto expectedHouseDto = getAvanHouseDto();
            expectedHouseDto.area = NEW_AREA;
            expectedHouseDto.country = NEW_COUNTRY;
            expectedHouseDto.street = NEW_STREET;
            House updatedHouse = getAvanHouse();
            updatedHouse.setArea(NEW_AREA);
            updatedHouse.setCountry(NEW_COUNTRY);
            updatedHouse.setStreet(NEW_STREET);

            //when
            when(houseDao.findByUuid(houseUuid))
                    .thenReturn(Optional.of(previousFoundHouse));
            when(houseDao.save(updatedHouse))
                    .thenReturn(updatedHouse);
            HouseDto actualHouseDto = houseService.update(houseUuid, expectedHouseDto);

            //then
            assertAll(
                    () -> assertNotNull(expectedHouseDto.uuid),
                    () -> assertEquals(expectedHouseDto.area, actualHouseDto.area),
                    () -> assertEquals(expectedHouseDto.number, actualHouseDto.number),
                    () -> assertEquals(expectedHouseDto.city, actualHouseDto.city),
                    () -> assertEquals(expectedHouseDto.country, actualHouseDto.country),
                    () -> assertEquals(expectedHouseDto.street, actualHouseDto.street)
            );
        }

        @Test
        void checkUpdateShouldThrowEntityNotFoundException() {
            //given
            HouseDto updatedHouseDto = getAvanHouseDto();

            //when
            when(houseDao.findByUuid(NOT_EXISTS_UUID))
                    .thenReturn(Optional.empty());

            //then
            assertAll(
                    () -> assertThrows(EntityNotFoundException.class,
                            () -> houseService.update(NOT_EXISTS_UUID, updatedHouseDto)),
                    () -> verify(houseDao, times(0)).save(any())
            );
        }

    }

    @Nested
    class DeleteByUuid {
        @Test
        void checkDeleteByUuidShouldDeleteHouse() {
            //given
            UUID deletedUuid = UUID_H_AVAN;
            int successfulCode = 1;

            //when
            when(houseDao.deleteByUuid(deletedUuid))
                    .thenReturn(successfulCode);
            boolean isDeleted = houseService.deleteByUuid(deletedUuid);

            //then
            assertTrue(isDeleted);
        }

        @Test
        void checkDeleteByUuidShouldReturnFalse() {
            //given
            UUID deletedUuid = UUID_H_AVAN;
            int successfulCode = 0;

            //when
            when(houseDao.deleteByUuid(deletedUuid))
                    .thenReturn(successfulCode);
            boolean isDeleted = houseService.deleteByUuid(deletedUuid);

            //then
            assertFalse(isDeleted);
        }
    }
}
