package ru.clevertec.houses.util;

import ru.clevertec.houses.dto.response.PaginationResponseDto;

import java.util.List;

public class PaginationResponseDtoTestData {
    public static PaginationResponseDto createResponse(int pageSize, int pageNumber, List<?> responseData) {
        PaginationResponseDto responseDto = new PaginationResponseDto();
        responseDto.pageSize = pageSize;
        responseDto.pageNumber = pageNumber;
        responseDto.data = List.copyOf(responseData);

        return responseDto;
    }

}
