package ru.clevertec.houses.util.dto;

import org.springframework.data.domain.Pageable;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.response.HouseHistoryDto;

import java.util.List;
import java.util.UUID;

public class HouseHistoryDtoTestData {

    public static HouseHistoryDto createHouseHistoryDto(UUID houseUuid, Pageable pageable, List<PersonDto> data) {
        HouseHistoryDto historyDto = new HouseHistoryDto();
        historyDto.houseUuid = houseUuid;
        historyDto.pageNumber = pageable.getPageNumber();
        historyDto.pageSize = pageable.getPageSize();
        historyDto.personDtoList = data;

        return historyDto;
    }

}
