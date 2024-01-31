package ru.clevertec.houses.util;

import org.springframework.data.domain.Pageable;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.response.PersonHistoryDto;

import java.util.List;
import java.util.UUID;

public class PersonHistoryDtoTestData {


    public static PersonHistoryDto createPersonHistoryDto(UUID personUuid, Pageable pageable, List<HouseDto> houseDtoList) {
        PersonHistoryDto historyDto = new PersonHistoryDto();
        historyDto.personUuid = personUuid;
        historyDto.pageNumber = pageable.getPageNumber();
        historyDto.pageSize = pageable.getPageSize();
        historyDto.houseDtoList = houseDtoList;

        return historyDto;
    }

}
