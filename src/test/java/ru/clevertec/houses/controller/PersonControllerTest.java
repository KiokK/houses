package ru.clevertec.houses.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.PaginationInfoDto;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.PersonsHouseDto;
import ru.clevertec.houses.dto.request.PersonsHouseRequestDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.dto.response.PersonHistoryDto;
import ru.clevertec.houses.exception.EntityNotFoundException;
import ru.clevertec.houses.model.enums.HistoryType;
import ru.clevertec.houses.service.PersonService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.clevertec.houses.dto.error.ErrorCodeConstants.ENTITY_NOT_FOUND;
import static ru.clevertec.houses.dto.error.ErrorCodeConstants.ENTITY_NOT_MODIFIED;
import static ru.clevertec.houses.dto.error.ErrorCodeConstants.REQUEST_NOT_VALID;
import static ru.clevertec.houses.util.ConstantsTest.EMPTY_STRING;
import static ru.clevertec.houses.util.ConstantsTest.INCORRECT_PASSPORT_NUMBER_SHORT;
import static ru.clevertec.houses.util.ConstantsTest.INCORRECT_PASSPORT_SERIES;
import static ru.clevertec.houses.util.ConstantsTest.NOT_EXISTS_UUID;
import static ru.clevertec.houses.util.ConstantsTest.PAGE_NUMBER_0;
import static ru.clevertec.houses.util.ConstantsTest.PAGE_SIZE_6;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_AVAN;
import static ru.clevertec.houses.util.ConstantsTest.UUID_P_AVAN;
import static ru.clevertec.houses.util.HouseDtoTestData.getListHouseDto;
import static ru.clevertec.houses.util.JsonConverterUtil.toJsonString;
import static ru.clevertec.houses.util.PaginationResponseDtoTestData.createResponse;
import static ru.clevertec.houses.util.PersonDtoTestData.getAvan;
import static ru.clevertec.houses.util.PersonDtoTestData.getListPersonDtoSize6;
import static ru.clevertec.houses.util.PersonDtoTestData.validPersonBuilder;
import static ru.clevertec.houses.util.PersonHistoryDtoTestData.createPersonHistoryDto;
import static ru.clevertec.houses.util.PersonsHouseDtoTestData.getPersonsHouseDto;
import static ru.clevertec.houses.util.PersonsHouseRequestDtoTestData.getPersonsHouseRequestDtoWith3Houses;

@WebMvcTest(PersonController.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class PersonControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private final PersonService personService;

    @Nested
    class FindAll {

        @Test
        @SneakyThrows
        void checkFindAllShouldReturnCorrectResponse() {
            //given
            List<PersonDto> persons = getListPersonDtoSize6();
            PaginationResponseDto responseDto = createResponse(PAGE_SIZE_6, PAGE_NUMBER_0, persons);
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_6);

            //when
            when(personService.findAll(pageable))
                    .thenReturn(responseDto);

            //then
            mockMvc.perform(get("/persons?pageNumber=0&pageSize=6"))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.pageNumber").value(PAGE_NUMBER_0),
                            jsonPath("$.pageSize").value(PAGE_SIZE_6),
                            jsonPath("$.data.size()").value(persons.size())
                    );
        }

        @Test
        @SneakyThrows
        void checkFindAllShouldReturnCorrectResponseByDefaultParams() {
            //given
            List<PersonDto> persons = getListPersonDtoSize6();
            PaginationInfoDto paginationInfo = new PaginationInfoDto();
            PaginationResponseDto responseDto = createResponse(paginationInfo.getPageSize(), paginationInfo.getPageNumber(), persons);
            Pageable pageable = PageRequest.of(paginationInfo.getPageNumber(), paginationInfo.getPageSize());

            //when
            when(personService.findAll(pageable))
                    .thenReturn(responseDto);

            //then
            mockMvc.perform(get("/persons"))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.pageNumber").value(PAGE_NUMBER_0),
                            jsonPath("$.pageSize").value(paginationInfo.getPageSize()),
                            jsonPath("$.data.size()").value(persons.size())
                    );
        }

        @Test
        @SneakyThrows
        void checkFindAllShouldCatchValidatedException() {
            //when//then
            mockMvc.perform(get("/persons?pageNumber=-1", "/persons?pageSize=0"))
                    .andExpectAll(
                            status().isBadRequest(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").value(REQUEST_NOT_VALID),
                            jsonPath("$.errorMessage").isNotEmpty()
                    );
            verify(personService, times(0)).findAll(any(Pageable.class));
        }
    }

    @Nested
    class FindAllHousesInWhichEverLive {

        @Test
        @SneakyThrows
        void checkFindAllHousesInWhichEverLiveShouldReturnCorrectDto() {
            //given
            List<HouseDto> testHouseDtos = getListHouseDto();
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_6);
            PersonHistoryDto houseHistoryAboutTenantsDto = createPersonHistoryDto(UUID_H_AVAN, pageable, testHouseDtos);

            //when
            when(personService.findHousesByPersonUuidAndHistoryType(UUID_H_AVAN, pageable, HistoryType.TENANT))
                    .thenReturn(houseHistoryAboutTenantsDto);

            //then
            mockMvc.perform(get(String.format("/persons/%s/history/housing?pageNumber=0&pageSize=6", UUID_H_AVAN)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.personUuid").value(UUID_H_AVAN.toString()),
                            jsonPath("$.pageNumber").value(PAGE_NUMBER_0),
                            jsonPath("$.pageSize").value(PAGE_SIZE_6),
                            jsonPath("$.houseDtoList.size()").value(testHouseDtos.size())
                    );
        }

        @Test
        @SneakyThrows
        void checkFindAllHousesInWhichEverLiveShouldReturnErrorDto() {
            //when
            when(personService.findHousesByPersonUuidAndHistoryType(eq(NOT_EXISTS_UUID), any(Pageable.class), eq(HistoryType.TENANT)))
                    .thenThrow(new EntityNotFoundException());

            //then
            mockMvc.perform(get(String.format("/persons/%s/history/housing?pageNumber=0&pageSize=6", NOT_EXISTS_UUID)))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").value(ENTITY_NOT_FOUND),
                            jsonPath("$.errorMessage").isNotEmpty()
                    );
        }
    }

    @Nested
    class HistoryAboutOwnerships {

        @Test
        @SneakyThrows
        void checkHistoryAboutOwnershipsShouldReturnCorrectResponse() {
            //given
            List<HouseDto> testHouseDtos = getListHouseDto();
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_6);
            PersonHistoryDto houseHistoryAboutOwnersDto = createPersonHistoryDto(UUID_H_AVAN, pageable, testHouseDtos);

            //when
            when(personService.findHousesByPersonUuidAndHistoryType(UUID_H_AVAN, pageable, HistoryType.OWNER))
                    .thenReturn(houseHistoryAboutOwnersDto);

            //then
            mockMvc.perform(get(String.format("/persons/%s/history/ownerships?pageNumber=0&pageSize=6", UUID_H_AVAN)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.personUuid").value(UUID_H_AVAN.toString()),
                            jsonPath("$.pageNumber").value(PAGE_NUMBER_0),
                            jsonPath("$.pageSize").value(PAGE_SIZE_6),
                            jsonPath("$.houseDtoList.size()").value(testHouseDtos.size())
                    );
        }

        @Test
        @SneakyThrows
        void checkHistoryAboutOwnershipsShouldReturnErrorDto() {
            //when
            when(personService.findHousesByPersonUuidAndHistoryType(eq(NOT_EXISTS_UUID), any(Pageable.class), eq(HistoryType.OWNER)))
                    .thenThrow(new EntityNotFoundException());

            //then
            mockMvc.perform(get(String.format("/persons/%s/history/ownerships?pageNumber=0&pageSize=6", NOT_EXISTS_UUID)))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").value(ENTITY_NOT_FOUND),
                            jsonPath("$.errorMessage").isNotEmpty()
                    );
        }
    }

    @Nested
    class FindPersonByUuid {

        @Test
        @SneakyThrows
        void checkFindPersonByUuidShouldReturnCorrectResponse() {
            //given
            UUID testUuid = UUID_P_AVAN;
            PersonDto testDto = getAvan();

            //when
            when(personService.findPersonByUuid(testUuid))
                    .thenReturn(testDto);

            //then
            mockMvc.perform(get(String.format("/persons/%s", testUuid)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.uuid").value(testUuid.toString()),
                            jsonPath("$.name").value(testDto.name),
                            jsonPath("$.surname").value(testDto.surname),
                            jsonPath("$.houseLiveUuid").value(testDto.houseLiveUuid.toString()),
                            jsonPath("$.passportNumber").value(testDto.passportNumber),
                            jsonPath("$.passportSeries").value(testDto.passportSeries),
                            jsonPath("$.sex").value(testDto.sex.toString())
                    );
        }

        @Test
        @SneakyThrows
        void checkFindPersonByUuidShouldReturnErrorDto() {
            //when
            when(personService.findPersonByUuid(NOT_EXISTS_UUID))
                    .thenThrow(new EntityNotFoundException(NOT_EXISTS_UUID));

            //then
            mockMvc.perform(get(String.format("/persons/%s", NOT_EXISTS_UUID)))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").value(ENTITY_NOT_FOUND),
                            jsonPath("$.errorMessage").isNotEmpty()
                    );
        }
    }

    @Nested
    class FindAllOwnHousesByPersonUuid {

        @Test
        @SneakyThrows
        void checkFindAllOwnHousesByPersonUuidShouldReturnCorrectResponse() {
            //given
            UUID testPersonUuid = UUID_P_AVAN;
            PersonsHouseDto personHousesDto = getPersonsHouseDto();
            int expectedResidentsCount = personHousesDto.ownHouses.size();

            //when
            when(personService.findAllOwnHousesByPersonUuid(testPersonUuid))
                    .thenReturn(personHousesDto);

            //then
            mockMvc.perform(get(String.format("/persons/%s/with_houses", testPersonUuid)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.uuid").value(testPersonUuid.toString()),
                            jsonPath("$.ownHouses").isArray(),
                            jsonPath("$.ownHouses.size()").value(expectedResidentsCount)
                    );
        }

        @Test
        @SneakyThrows
        void checkFindAllOwnHousesByPersonUuidShouldReturnErrorDto() {
            //when
            when(personService.findAllOwnHousesByPersonUuid(NOT_EXISTS_UUID))
                    .thenThrow(new EntityNotFoundException());

            //then
            mockMvc.perform(get(String.format("/persons/%s/with_houses", NOT_EXISTS_UUID)))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").value(ENTITY_NOT_FOUND),
                            jsonPath("$.errorMessage").isNotEmpty()
                    );
        }
    }


    @Nested
    class CreatePerson {

        @Test
        @SneakyThrows
        void checkCreatePersonShouldReturnCorrectResponse() {
            //given
            PersonDto testDto = getAvan();
            testDto.uuid = null;
            String jsonNewPersonDto = toJsonString(testDto);
            PersonDto expectedDto = getAvan();

            //when
            when(personService.create(testDto))
                    .thenReturn(expectedDto);

            //then
            mockMvc.perform(post("/persons/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonNewPersonDto))
                    .andExpectAll(
                            status().is2xxSuccessful(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.uuid").value(expectedDto.uuid.toString())
                    );
        }

        @Test
        @SneakyThrows
        void checkCreatePersonShouldHandleValidationException() {
            //given
            PersonDto invalidPersonDto = validPersonBuilder()
                    .name(EMPTY_STRING)
                    .passportSeries(INCORRECT_PASSPORT_SERIES)
                    .passportNumber(INCORRECT_PASSPORT_NUMBER_SHORT)
                    .build();
            String jsonIncorrectPersonDto = toJsonString(invalidPersonDto);

            //when//then
            mockMvc.perform(post("/persons/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonIncorrectPersonDto))
                    .andExpectAll(
                            status().isBadRequest(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").value(REQUEST_NOT_VALID),
                            jsonPath("$.errorMessage").isNotEmpty()
                    );
            verify(personService, times(0)).create(any());
        }
    }

    @Nested
    class UpdatePersonWithHousesDto {

        @Test
        @SneakyThrows
        void checkUpdatePersonWithHousesDtoShouldReturnStatusUpdated() {
            //given
            PersonsHouseRequestDto requestDto = getPersonsHouseRequestDtoWith3Houses();
            String jsonPersonDtoRequest = toJsonString(requestDto);
            UUID testUuid = requestDto.personUuid;

            //when
            when(personService.update(testUuid, requestDto))
                    .thenReturn(true);

            //then
            mockMvc.perform(put(String.format("/persons/%s/update/houses", testUuid))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPersonDtoRequest))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void checkUpdatePersonWithHousesDtoShouldHandleValidationException() {
            //given
            PersonsHouseRequestDto requestDto = getPersonsHouseRequestDtoWith3Houses();
            requestDto.houseUuids = null;
            String jsonPersonDtoRequest = toJsonString(requestDto);
            UUID testUuid = requestDto.personUuid;

            //when//then
            mockMvc.perform(put(String.format("/persons/%s/update/houses", testUuid))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPersonDtoRequest))
                    .andExpectAll(
                            status().isBadRequest(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").value(REQUEST_NOT_VALID),
                            jsonPath("$.errorMessage").isNotEmpty()
                    );
            verify(personService, times(0)).update(any(UUID.class), any(PersonsHouseRequestDto.class));
        }

    }

    @Nested
    class UpdatePersonInfoDto {

        @Test
        @SneakyThrows
        void checkUpdatePersonInfoDtoShouldReturnCorrectResponse() {
            //given
            UUID testUuid = UUID_P_AVAN;
            PersonDto testDto = getAvan();
            String jsonPersonDtoRequest = toJsonString(testDto);
            PersonDto expectedDto = getAvan();

            //when
            when(personService.update(testUuid, testDto))
                    .thenReturn(expectedDto);

            //then
            mockMvc.perform(put(String.format("/persons/%s/update", testUuid))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonPersonDtoRequest))
                    .andExpectAll(
                            status().is2xxSuccessful(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.uuid").value(expectedDto.uuid.toString()),
                            jsonPath("$.name").value(expectedDto.name),
                            jsonPath("$.surname").value(expectedDto.surname),
                            jsonPath("$.sex").value(expectedDto.sex.toString()),
                            jsonPath("$.passportSeries").value(expectedDto.passportSeries),
                            jsonPath("$.passportNumber").value(expectedDto.passportNumber),
                            jsonPath("$.houseLiveUuid").value(expectedDto.houseLiveUuid.toString())
                    );
        }
    }

    @Nested
    class DeletePersonByUuid {

        @Test
        @SneakyThrows
        void checkDeletePersonByUuidIsTrueShouldReturnCorrectStatus() {
            //given
            UUID testUuid = UUID_P_AVAN;

            //when
            when(personService.deleteByUuid(testUuid))
                    .thenReturn(true);

            //then
            mockMvc.perform(delete("/persons/delete")
                            .param("uuid", testUuid.toString()))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void checkDeletePersonByUuidIsFalseShouldReturnErrorDto() {
            //given
            UUID testUuid = UUID_P_AVAN;

            //when
            when(personService.deleteByUuid(testUuid))
                    .thenReturn(false);

            //then
            mockMvc.perform(delete("/persons/delete")
                            .param("uuid", testUuid.toString()))
                    .andExpectAll(
                            status().isBadRequest(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorMessage").isNotEmpty(),
                            jsonPath("$.errorCode").value(ENTITY_NOT_MODIFIED)
                    );
        }
    }
}
