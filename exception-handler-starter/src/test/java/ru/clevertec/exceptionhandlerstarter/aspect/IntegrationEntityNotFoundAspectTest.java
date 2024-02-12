package ru.clevertec.exceptionhandlerstarter.aspect;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.ResponseEntity;
import ru.clevertec.exceptionhandlerstarter.domain.ErrorResponseDto;
import ru.clevertec.exceptionhandlerstarter.util.AnnotatedClassUtil;
import ru.clevertec.exceptionhandlerstarter.util.Dto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException.MESSAGE_WITH_UUID;
import static ru.clevertec.exceptionhandlerstarter.util.Constants.TEST_ERROR_CODE;
import static ru.clevertec.exceptionhandlerstarter.util.Constants.TEST_HTTP_STATUS;

@SpringBootTest
class IntegrationEntityNotFoundAspectTest {


    @Autowired
    private AnnotatedClassUtil controllerAnnotatedClass;

    @SpyBean
    private EntityNotFoundExceptionAspect aspect;

    @Test
    public void checkReturnErrorDtoVerifyFromAnnotatedClass() throws Throwable {
        //given
        boolean isThrowing = true;
        int statusCode = TEST_HTTP_STATUS;
        UUID notFoundUuid = UUID.randomUUID();
        String errorMessage = String.format(MESSAGE_WITH_UUID, notFoundUuid);
        ErrorResponseDto expectedErrorResponseDto = new ErrorResponseDto(errorMessage, TEST_ERROR_CODE);
        ResponseEntity.status(statusCode).body(expectedErrorResponseDto);

        //when
        ResponseEntity<?> actual = controllerAnnotatedClass.method(notFoundUuid, isThrowing);
        ErrorResponseDto actualDto = (ErrorResponseDto) actual.getBody();

        //then
        assertAll(
                () -> verify(aspect).returnErrorDtoWhenCatchEntityNotFoundException(any(), any()),
                () -> assertEquals(expectedErrorResponseDto.errorCode, actualDto.errorCode),
                () -> assertEquals(expectedErrorResponseDto.errorMessage, actualDto.errorMessage),
                () -> assertEquals(statusCode, actual.getStatusCode().value())
        );
    }
    @Test
    public void checkReturnDtoVerifyFromAnnotatedClass() throws Throwable {
        //given
        boolean isThrowing = false;
        UUID foundUuid = UUID.randomUUID();
        Dto expectedDto = new Dto(foundUuid);

        //when
        ResponseEntity<?> actual = controllerAnnotatedClass.method(foundUuid, isThrowing);
        Dto actualDto = (Dto) actual.getBody();

        //then
        assertAll(
                () -> verify(aspect).returnErrorDtoWhenCatchEntityNotFoundException(any(), any()),
                () -> assertEquals(expectedDto, actualDto)
        );
    }
}
