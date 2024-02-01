package ru.clevertec.houses.util;

import ru.clevertec.houses.dto.error.ErrorResponseDto;

import java.util.UUID;

import static ru.clevertec.houses.dto.error.ErrorCodeConstants.ENTITY_NOT_FOUND;
import static ru.clevertec.houses.dto.error.ErrorCodeConstants.ENTITY_NOT_MODIFIED;
import static ru.clevertec.houses.dto.error.ErrorMessagesConstants.M_NOT_DELETED;
import static ru.clevertec.houses.util.ConstantsTest.DEFAULT_ERROR_MESSAGE;

public class ErrorResponseDtoTestData {

    public static ErrorResponseDto getErrorResponseDtoNoDeleted(UUID uuid) {
        return new ErrorResponseDto(String.format(M_NOT_DELETED, "uuid", uuid), ENTITY_NOT_MODIFIED);
    }

    public static ErrorResponseDto getErrorResponseDtoNotFound() {
        return new ErrorResponseDto(DEFAULT_ERROR_MESSAGE, ENTITY_NOT_FOUND);
    }
}
