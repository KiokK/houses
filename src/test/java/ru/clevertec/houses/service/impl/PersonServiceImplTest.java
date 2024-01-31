package ru.clevertec.houses.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.houses.dao.HouseDao;
import ru.clevertec.houses.dao.PersonDao;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.PersonsHouseDto;
import ru.clevertec.houses.dto.request.PersonsHouseRequestDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.dto.response.PersonHistoryDto;
import ru.clevertec.houses.exception.EntityNotFoundException;
import ru.clevertec.houses.model.House;
import ru.clevertec.houses.model.Person;
import ru.clevertec.houses.model.enums.HistoryType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.houses.util.ConstantsTest.NEW_NAME;
import static ru.clevertec.houses.util.ConstantsTest.NEW_SURNAME;
import static ru.clevertec.houses.util.ConstantsTest.NOT_EXISTS_UUID;
import static ru.clevertec.houses.util.ConstantsTest.PAGE_NUMBER_0;
import static ru.clevertec.houses.util.ConstantsTest.PAGE_SIZE_6;
import static ru.clevertec.houses.util.ConstantsTest.UUID_P_AVAN;
import static ru.clevertec.houses.util.HouseDtoTestData.getListOfTwoHouseDto;
import static ru.clevertec.houses.util.HouseTestData.getAvanHouse;
import static ru.clevertec.houses.util.HouseTestData.getBvanHouse;
import static ru.clevertec.houses.util.HouseTestData.getCvanHouse;
import static ru.clevertec.houses.util.HouseTestData.getListOfTwoHouses;
import static ru.clevertec.houses.util.PaginationResponseDtoTestData.createResponse;
import static ru.clevertec.houses.util.PersonDtoTestData.getAvan;
import static ru.clevertec.houses.util.PersonHistoryDtoTestData.createPersonHistoryDto;
import static ru.clevertec.houses.util.PersonTestData.getListPersonSize2;
import static ru.clevertec.houses.util.PersonTestData.getPersonAvan;
import static ru.clevertec.houses.util.PersonsHouseDtoTestData.getPersonsHouseDto;
import static ru.clevertec.houses.util.PersonsHouseRequestDtoTestData.getPersonsHouseRequestDtoWith3Houses;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @InjectMocks
    private PersonServiceImpl personService;

    @Mock
    private PersonDao personDao;

    @Mock
    private HouseDao houseDao;

    @Nested
    class Create {

        @Test
        void checkCreateShouldReturnCreatedDto() {
            //given
            PersonDto expectedPersonDto = getAvan();
            House personLivePlace = getAvanHouse();
            Person createdPerson = getPersonAvan();

            //when
            when(houseDao.findByUuid(personLivePlace.getUuid()))
                    .thenReturn(Optional.of(personLivePlace));
            when(personDao.save(any(Person.class)))
                    .thenReturn(createdPerson);
            PersonDto actualPersonDto = personService.create(expectedPersonDto);

            //then
            assertEquals(expectedPersonDto, actualPersonDto);
        }

        @Test
        void checkCreateWithNoExistHouseShouldThrowEntityNotFoundException() {
            //given
            PersonDto newPerson = getAvan();
            newPerson.houseLiveUuid = NOT_EXISTS_UUID;

            //when
            when(houseDao.findByUuid(NOT_EXISTS_UUID))
                    .thenReturn(Optional.empty());

            //then
            assertAll(
                    () -> assertThrows(EntityNotFoundException.class, () -> personService.create(newPerson)),
                    () -> verify(personDao, times(0)).save(any())
            );
        }
    }

    @Nested
    class FindPersonByUuid {
        @Test
        void checkFindPersonByUuidShouldThrowsEntityNotFoundException() {
            //when
            when(personDao.findByUuid(NOT_EXISTS_UUID))
                    .thenReturn(Optional.empty());

            //then
            assertThrows(EntityNotFoundException.class,
                    () -> personService.findPersonByUuid(NOT_EXISTS_UUID));
        }

        @Test
        void checkFindPersonByUuidShouldReturnFoundedPersonDto() {
            //given
            PersonDto expectedPersonDto = getAvan();
            Person foundedPerson = getPersonAvan();
            UUID testUuid = expectedPersonDto.uuid;

            //when
            when(personDao.findByUuid(testUuid))
                    .thenReturn(Optional.of(foundedPerson));
            PersonDto actualPersonDto = personService.findPersonByUuid(testUuid);

            //then
            assertEquals(expectedPersonDto, actualPersonDto);
        }
    }

    @Nested
    class FindAll {

        @Test
        void checkFindAllShouldReturnCorrectPaginationResponse() {
            //given
            List<Person> paginationPersons = getListPersonSize2();
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_6);
            PaginationResponseDto expected = createResponse(PAGE_SIZE_6, PAGE_NUMBER_0, paginationPersons);

            //when
            when(personDao.findAll(pageable)).
                    thenReturn(new PageImpl<>(paginationPersons));
            PaginationResponseDto actual = personService.findAll(pageable);

            //then
            assertAll(
                    () -> assertEquals(expected.pageNumber, actual.pageNumber),
                    () -> assertEquals(expected.pageSize, actual.pageSize),
                    () -> assertEquals(expected.data.size(), actual.data.size())
            );
        }
    }

    @Nested
    class FindAllOwnHousesByPersonUuid {
        @Test
        void checkFindAllOwnHousesByPersonUuidShouldThrowEntityNotFoundException() {
            //when
            when(personDao.findWithHousesByPersonUuid(NOT_EXISTS_UUID))
                    .thenReturn(Optional.empty());

            //then
            assertThrows(EntityNotFoundException.class,
                    () -> personService.findAllOwnHousesByPersonUuid(NOT_EXISTS_UUID));
        }

        @Test
        void checkFindAllOwnHousesByPersonUuidShouldReturnPersonsHouseDto() {
            //given
            PersonsHouseDto expectedPersonWithHouses = getPersonsHouseDto();
            UUID personTestUuid = expectedPersonWithHouses.uuid;
            Person foundedPerson = getPersonAvan();
            foundedPerson.setHouses(List.of(getAvanHouse()));

            //when
            when(personDao.findWithHousesByPersonUuid(personTestUuid))
                    .thenReturn(Optional.of(foundedPerson));
            PersonsHouseDto actualPersonWithHouses = personService.findAllOwnHousesByPersonUuid(personTestUuid);

            //then
            assertAll(
                    () -> assertEquals(expectedPersonWithHouses.uuid, actualPersonWithHouses.uuid),
                    () -> assertEquals(expectedPersonWithHouses.ownHouses.size(), actualPersonWithHouses.ownHouses.size())
            );
        }
    }

    @Nested
    class FindHousesByPersonUuidAndHistoryType {
        @Test
        void checkFindHousesByPersonUuidAndHistoryTypeShouldThrowEntityNotFoundException() {
            //given
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_6);
            HistoryType historyType = HistoryType.OWNER;

            //when
            when(personDao.findByUuid(NOT_EXISTS_UUID))
                    .thenReturn(Optional.empty());

            //then
            assertThrows(EntityNotFoundException.class,
                    () -> personService.findHousesByPersonUuidAndHistoryType(NOT_EXISTS_UUID, pageable, historyType));
        }

        @Test
        void checkFindHousesByPersonUuidAndHistoryTypeShouldReturnPaginationHistoryDto() {
            //given
            UUID testUuid = UUID_P_AVAN;
            Person foundedPerson = getPersonAvan();
            List<House> foundedHouses = getListOfTwoHouses();
            foundedPerson.setHouses(foundedHouses);
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_6);
            HistoryType historyType = HistoryType.OWNER;
            List<HouseDto> houseDtos = getListOfTwoHouseDto();
            PersonHistoryDto expectedHistory = createPersonHistoryDto(testUuid, pageable, houseDtos);

            //when
            when(personDao.findByUuid(testUuid))
                    .thenReturn(Optional.of(foundedPerson));
            when(houseDao.findAllByHouseHistory_person_uuidAndHouseHistory_type(testUuid, historyType, pageable))
                    .thenReturn(foundedHouses);
            PersonHistoryDto actualHistory = personService.findHousesByPersonUuidAndHistoryType(testUuid, pageable, historyType);

            //then
            assertAll(
                    () -> assertEquals(expectedHistory.pageSize, actualHistory.pageSize),
                    () -> assertEquals(expectedHistory.pageNumber, actualHistory.pageNumber),
                    () -> assertEquals(expectedHistory.personUuid, actualHistory.personUuid),
                    () -> assertEquals(expectedHistory.houseDtoList.size(), actualHistory.houseDtoList.size())
            );
        }
    }

    @Nested
    class UpdateByPersonDto {

        @Test
        void checkUpdateShouldThrowsEntityNotFoundException() {
            //when
            when(personDao.findByUuid(NOT_EXISTS_UUID))
                    .thenReturn(Optional.empty());

            //then
            assertAll(
                    () -> assertThrows(EntityNotFoundException.class,
                            () -> personService.update(NOT_EXISTS_UUID, new PersonDto())),
                    () -> verify(houseDao, times(0)).findByUuid(any()),
                    () -> verify(personDao, times(0)).save(any())
            );
        }

        @Test
        void checkUpdateWithNoExistHouseShouldThrowsEntityNotFoundException() {
            //given
            PersonDto updateDto = getAvan();
            updateDto.houseLiveUuid = NOT_EXISTS_UUID;
            Person foundedPerson = getPersonAvan();

            //when
            when(personDao.findByUuid(UUID_P_AVAN))
                    .thenReturn(Optional.of(foundedPerson));
            when(houseDao.findByUuid(NOT_EXISTS_UUID))
                    .thenReturn(Optional.empty());

            //then
            assertAll(
                    () -> assertThrows(EntityNotFoundException.class,
                            () -> personService.update(UUID_P_AVAN, updateDto)),
                    () -> verify(personDao, times(0)).save(any())
            );
        }

        @Test
        void checkUpdateShouldReturnPersonDto() {
            //given
            UUID testUuid = UUID_P_AVAN;
            Person foundedPerson = getPersonAvan();
            PersonDto expectedPersonDto = getAvan();
            expectedPersonDto.name = NEW_NAME;
            expectedPersonDto.surname = NEW_SURNAME;
            Person savedPerson = getPersonAvan();
            savedPerson.setName(NEW_NAME);
            savedPerson.setSurname(NEW_SURNAME);

            //when
            when(personDao.findByUuid(testUuid))
                    .thenReturn(Optional.of(foundedPerson));
            when(houseDao.findByUuid(savedPerson.getResidentOf().getUuid()))
                    .thenReturn(Optional.ofNullable(savedPerson.getResidentOf()));
            when(personDao.save(savedPerson))
                    .thenReturn(savedPerson);
            PersonDto actualPersonDto = personService.update(testUuid, expectedPersonDto);

            //then
            assertEquals(expectedPersonDto, actualPersonDto);
        }
    }

    @Nested
    class UpdateByPersonsHouseRequestDto {
        @Test
        void checkUpdateShouldReturnTrue() {
            //given
            PersonsHouseRequestDto updatedRequest = getPersonsHouseRequestDtoWith3Houses();
            UUID testUuid = UUID_P_AVAN;
            Person foundedPerson = getPersonAvan();
            List<House> newHouses = List.of(getAvanHouse(), getBvanHouse(), getCvanHouse());
            Person savedPerson = getPersonAvan();
            savedPerson.setHouses(newHouses);

            //when
            when(personDao.findByUuid(testUuid))
                    .thenReturn(Optional.of(foundedPerson));
            when(houseDao.findAllByUuidIn(updatedRequest.houseUuids))
                    .thenReturn(newHouses);
            when(personDao.save(savedPerson))
                    .thenReturn(savedPerson);
            boolean isUpdated = personService.update(testUuid, updatedRequest);

            //then
            assertTrue(isUpdated);
        }

        @Test
        void checkUpdateShouldThrowsEntityNotFoundException() {
            //when
            when(personDao.findByUuid(NOT_EXISTS_UUID))
                    .thenReturn(Optional.empty());

            //then
            assertAll(
                    () -> assertThrows(EntityNotFoundException.class,
                            () -> personService.update(NOT_EXISTS_UUID, new PersonsHouseRequestDto())),
                    () -> verify(houseDao, times(0)).findAllByUuidIn(any()),
                    () -> verify(personDao, times(0)).save(any())
            );
        }

    }

    @Nested
    class DeleteByUuid {
        @Test
        void checkDeleteByUuidShouldDeletePerson() {
            //given
            UUID deletedUuid = UUID_P_AVAN;
            int successfulCode = 1;

            //when
            when(personDao.deletePersonByUuid(deletedUuid))
                    .thenReturn(successfulCode);
            boolean isDeleted = personService.deleteByUuid(deletedUuid);

            //then
            assertTrue(isDeleted);
        }

        @Test
        void checkDeleteByUuidShouldReturnFalse() {
            //given
            UUID deletedUuid = UUID_P_AVAN;
            int successfulCode = 0;

            //when
            when(personDao.deletePersonByUuid(deletedUuid))
                    .thenReturn(successfulCode);
            boolean isDeleted = personService.deleteByUuid(deletedUuid);

            //then
            assertFalse(isDeleted);
        }
    }
}
