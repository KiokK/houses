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
import ru.clevertec.houses.dto.HouseResidentsDto;
import ru.clevertec.houses.dto.PaginationInfoDto;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.response.HouseHistoryDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.exception.EntityNotFoundException;
import ru.clevertec.houses.model.enums.HistoryType;
import ru.clevertec.houses.service.impl.HouseServiceImpl;

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
import static ru.clevertec.houses.util.ConstantsTest.NOT_EXISTS_UUID;
import static ru.clevertec.houses.util.ConstantsTest.PAGE_NUMBER_0;
import static ru.clevertec.houses.util.ConstantsTest.PAGE_SIZE_6;
import static ru.clevertec.houses.util.ConstantsTest.UUID_H_AVAN;
import static ru.clevertec.houses.util.HouseDtoTestData.getAvanHouseDto;
import static ru.clevertec.houses.util.HouseDtoTestData.getListHouseDto;
import static ru.clevertec.houses.util.HouseHistoryDtoTestData.createHouseHistoryDto;
import static ru.clevertec.houses.util.HouseResidentsDtoTestData.getAvanHouseResidentsDto;
import static ru.clevertec.houses.util.JsonConverterUtil.toJsonString;
import static ru.clevertec.houses.util.PaginationResponseDtoTestData.createResponse;
import static ru.clevertec.houses.util.PersonDtoTestData.getListPersonDtoSize6;

@WebMvcTest(HouseController.class)
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class HouseControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private final HouseServiceImpl houseService;

    @Nested
    class FindAll {

        @Test
        @SneakyThrows
        void checkFindAllShouldReturnCorrectResponse() {
            //given
            PaginationResponseDto responseDto = createResponse(PAGE_SIZE_6, PAGE_NUMBER_0, getListHouseDto());
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_6);

            //when
            when(houseService.findAll(pageable))
                    .thenReturn(responseDto);

            //then
            mockMvc.perform(get("/houses?pageNumber=0&pageSize=6"))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.pageNumber").value(PAGE_NUMBER_0),
                            jsonPath("$.pageSize").value(PAGE_SIZE_6),
                            jsonPath("$.data.size()").value(getListHouseDto().size())
                    );
        }

        @Test
        @SneakyThrows
        void checkFindAllShouldReturnCorrectResponseByDefaultParams() {
            //given
            PaginationInfoDto paginationInfo = new PaginationInfoDto();
            PaginationResponseDto responseDto = createResponse(paginationInfo.getPageSize(), paginationInfo.getPageNumber(), getListHouseDto());
            Pageable pageable = PageRequest.of(paginationInfo.getPageNumber(), paginationInfo.getPageSize());

            //when
            when(houseService.findAll(pageable))
                    .thenReturn(responseDto);
            //then
            mockMvc.perform(get("/houses"))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.pageNumber").value(PAGE_NUMBER_0),
                            jsonPath("$.pageSize").value(paginationInfo.getPageSize()),
                            jsonPath("$.data.size()").value(getListHouseDto().size())
                    );
        }

        @Test
        @SneakyThrows
        void checkFindAllShouldCatchValidatedException() {
            //when//then
            mockMvc.perform(get("/houses?pageNumber=-1", "/houses?pageSize=0"))
                    .andExpectAll(
                            status().isBadRequest(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").value(REQUEST_NOT_VALID),
                            jsonPath("$.errorMessage").isNotEmpty()
                    );
            verify(houseService, times(0)).findAll(any(Pageable.class));
        }
    }

    @Nested
    class FindAllWhichEverLiveInHouse {

        @Test
        @SneakyThrows
        void checkFindAllWhichEverLiveInHouseCorrect() {
            //given
            List<PersonDto> testPersonDtoList = getListPersonDtoSize6();
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_6);
            HouseHistoryDto houseHistoryAboutTenantsDto = createHouseHistoryDto(UUID_H_AVAN, pageable, testPersonDtoList);

            //when
            when(houseService.findPersonsByHouseUuidAndHistoryType(UUID_H_AVAN, pageable, HistoryType.TENANT))
                    .thenReturn(houseHistoryAboutTenantsDto);

            //then
            mockMvc.perform(get(String.format("/houses/%s/history/tenants?pageNumber=0&pageSize=6", UUID_H_AVAN)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.houseUuid").value(UUID_H_AVAN.toString()),
                            jsonPath("$.pageNumber").value(PAGE_NUMBER_0),
                            jsonPath("$.pageSize").value(PAGE_SIZE_6),
                            jsonPath("$.personDtoList.size()").value(testPersonDtoList.size())
                    );
        }

        @Test
        @SneakyThrows
        void checkFindAllWhichEverLiveInHouseShouldReturnErrorDto() {
            //when
            when(houseService.findPersonsByHouseUuidAndHistoryType(eq(NOT_EXISTS_UUID), any(Pageable.class), eq(HistoryType.TENANT)))
                    .thenThrow(new EntityNotFoundException());

            //then
            mockMvc.perform(get(String.format("/houses/%s/history/tenants?pageNumber=0&pageSize=6", NOT_EXISTS_UUID)))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").value(ENTITY_NOT_FOUND),
                            jsonPath("$.errorMessage").isNotEmpty()
                    );
        }
    }

    @Nested
    class FindAllHistoryOwnersInHouse {

        @Test
        @SneakyThrows
        void checkFindAllHistoryOwnersInHouseShouldReturnCorrectResponse() {
            //given
            List<PersonDto> testPersonDtoList = getListPersonDtoSize6();
            Pageable pageable = PageRequest.of(PAGE_NUMBER_0, PAGE_SIZE_6);
            HouseHistoryDto houseHistoryAboutTenantsDto = createHouseHistoryDto(UUID_H_AVAN, pageable, testPersonDtoList);

            //when
            when(houseService.findPersonsByHouseUuidAndHistoryType(UUID_H_AVAN, pageable, HistoryType.OWNER))
                    .thenReturn(houseHistoryAboutTenantsDto);

            //then
            mockMvc.perform(get(String.format("/houses/%s/history/owners?pageNumber=0&pageSize=6", UUID_H_AVAN)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.houseUuid").value(UUID_H_AVAN.toString()),
                            jsonPath("$.pageNumber").value(PAGE_NUMBER_0),
                            jsonPath("$.pageSize").value(PAGE_SIZE_6),
                            jsonPath("$.personDtoList.size()").value(testPersonDtoList.size())
                    );
        }

        @Test
        @SneakyThrows
        void checkFindAllHistoryOwnersInHouseShouldReturnErrorDto() {
            //when
            when(houseService.findPersonsByHouseUuidAndHistoryType(eq(NOT_EXISTS_UUID), any(Pageable.class), eq(HistoryType.OWNER)))
                    .thenThrow(new EntityNotFoundException());

            //then
            mockMvc.perform(get(String.format("/houses/%s/history/owners?pageNumber=0&pageSize=6", NOT_EXISTS_UUID)))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").value(ENTITY_NOT_FOUND),
                            jsonPath("$.errorMessage").isNotEmpty()
                    );
        }
    }

    @Nested
    class FindHouseByUuid {

        @Test
        @SneakyThrows
        void checkFindHouseByUuidShouldReturnCorrectResponse() {
            //given
            HouseDto testDto = getAvanHouseDto();

            //when
            when(houseService.findHouseByUuid(UUID_H_AVAN))
                    .thenReturn(testDto);

            //then
            mockMvc.perform(get(String.format("/houses/%s", UUID_H_AVAN)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.uuid").value(UUID_H_AVAN.toString()),
                            jsonPath("$.area").value(testDto.area),
                            jsonPath("$.country").value(testDto.country),
                            jsonPath("$.city").value(testDto.city),
                            jsonPath("$.street").value(testDto.street),
                            jsonPath("$.number").value(testDto.number)
                    );
        }

        @Test
        @SneakyThrows
        void checkFindHouseByUuidShouldReturnErrorDto() {
            //when
            when(houseService.findHouseByUuid(NOT_EXISTS_UUID))
                    .thenThrow(new EntityNotFoundException(NOT_EXISTS_UUID));

            //then
            mockMvc.perform(get(String.format("/houses/%s", NOT_EXISTS_UUID)))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").value(ENTITY_NOT_FOUND),
                            jsonPath("$.errorMessage").isNotEmpty()
                    );
        }
    }

    @Nested
    class FindHouseResidentsByHouseUuid {

        @Test
        @SneakyThrows
        void checkFindHouseResidentsByHouseUuidShouldReturnCorrectResponse() {
            //given
            UUID testUuid = UUID_H_AVAN;
            HouseResidentsDto houseResidentsDto = getAvanHouseResidentsDto();
            int expectedResidentsCount = houseResidentsDto.residents.size();

            //when
            when(houseService.findAllResidentsByHouseUuid(testUuid))
                    .thenReturn(houseResidentsDto);

            //then
            mockMvc.perform(get(String.format("/houses/%s/with_residents", testUuid)))
                    .andExpectAll(
                            status().isOk(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.uuid").value(testUuid.toString()),
                            jsonPath("$.residents").isArray(),
                            jsonPath("$.residents.size()").value(expectedResidentsCount)
                    );
        }

        @Test
        @SneakyThrows
        void checkFindHouseResidentsByHouseUuidShouldReturnErrorDto() {
            //when
            when(houseService.findAllResidentsByHouseUuid(NOT_EXISTS_UUID))
                    .thenThrow(new EntityNotFoundException());

            //then
            mockMvc.perform(get(String.format("/houses/%s/with_residents", NOT_EXISTS_UUID)))
                    .andExpectAll(
                            status().is4xxClientError(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.errorCode").value(ENTITY_NOT_FOUND),
                            jsonPath("$.errorMessage").isNotEmpty()
                    );
        }
    }

    @Nested
    class CreateHouseDto {

        @Test
        @SneakyThrows
        void checkCreateHouseDtoShouldReturnCorrectResponse() {
            //given
            HouseDto testDto = getAvanHouseDto();
            testDto.uuid = null;
            String jsonHouseDtoRequest = toJsonString(testDto);
            HouseDto expectedDto = getAvanHouseDto();

            //when
            when(houseService.create(testDto))
                    .thenReturn(expectedDto);

            //then
            mockMvc.perform(post("/houses/create")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonHouseDtoRequest))
                    .andExpectAll(
                            status().is2xxSuccessful(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.uuid").value(expectedDto.uuid.toString())
                    );
        }
    }

    @Nested
    class UpdateHouseDto {

        @Test
        @SneakyThrows
        void checkUpdateHouseDtoShouldReturnCorrectResponse() {
            //given
            UUID testUuid = UUID_H_AVAN;
            HouseDto testDto = getAvanHouseDto();
            String jsonHouseDtoRequest = toJsonString(testDto);
            HouseDto expectedDto = getAvanHouseDto();

            //when
            when(houseService.update(testUuid, testDto))
                    .thenReturn(expectedDto);

            //then
            mockMvc.perform(put(String.format("/houses/%s/update", testUuid))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonHouseDtoRequest))
                    .andExpectAll(
                            status().is2xxSuccessful(),
                            content().contentType(MediaType.APPLICATION_JSON),
                            jsonPath("$.uuid").value(expectedDto.uuid.toString()),
                            jsonPath("$.street").value(expectedDto.street),
                            jsonPath("$.area").value(expectedDto.area),
                            jsonPath("$.country").value(expectedDto.country)
                    );
        }
    }

    @Nested
    class DeleteHouseByUuid {

        @Test
        @SneakyThrows
        void checkDeleteHouseByUuidIsTrueShouldReturnCorrectStatus() {
            //given
            UUID testUuid = UUID_H_AVAN;

            //when
            when(houseService.deleteByUuid(testUuid))
                    .thenReturn(true);

            //then
            mockMvc.perform(delete("/houses/delete")
                            .param("uuid", testUuid.toString()))
                    .andExpect(status().isNoContent());
        }

        @Test
        @SneakyThrows
        void checkDeleteHouseByUuidIsFalseShouldReturnErrorDto() {
            //given
            UUID testUuid = UUID_H_AVAN;

            //when
            when(houseService.deleteByUuid(testUuid))
                    .thenReturn(false);

            //then
            mockMvc.perform(delete("/houses/delete")
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
