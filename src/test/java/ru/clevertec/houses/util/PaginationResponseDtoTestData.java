package ru.clevertec.houses.util;

import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import ru.clevertec.houses.dto.response.PaginationResponseDto;

import java.util.List;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static ru.clevertec.houses.util.ConstantsTest.POSITIVE_INT_REGEX;

public class PaginationResponseDtoTestData {
    public static PaginationResponseDto createResponse(int pageSize, int pageNumber, List<?> responseData) {
        PaginationResponseDto responseDto = new PaginationResponseDto();
        responseDto.pageSize = pageSize;
        responseDto.pageNumber = pageNumber;
        responseDto.data = List.copyOf(responseData);

        return responseDto;
    }

    public static Map<String, StringValuePattern> getPaginationRegexUrlParams() {
        return Map.of("pageSize", matching(POSITIVE_INT_REGEX),
                "pageNumber", matching(POSITIVE_INT_REGEX));
    }

}
