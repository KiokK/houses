package ru.clevertec.exceptionhandlerstarter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import ru.clevertec.exceptionhandlerstarter.annotation.EntityNotFoundResponse;
import ru.clevertec.exceptionhandlerstarter.domain.ErrorResponseDto;
import ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException;
import ru.clevertec.exceptionhandlerstarter.util.Dto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.exceptionhandlerstarter.domain.ErrorCodeConstants.ENTITY_NOT_FOUND;
import static ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException.MESSAGE_DEFAULT;
import static ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException.MESSAGE_WITH_UUID;

@ExtendWith(MockitoExtension.class)
class EntityNotFoundExceptionAspectTest {

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private EntityNotFoundResponse entityNotFoundResponse;

    @InjectMocks
    private EntityNotFoundExceptionAspect entityNotFoundExceptionAspect;

    @Test
    void returnErrorDtoWhenCatchEntityNotFoundExceptionShouldReturnErrorDto() throws Throwable {
        //given
        int statusCode = 404;
        UUID notFoundUuid = UUID.randomUUID();
        String errorMessage = String.format(MESSAGE_WITH_UUID, notFoundUuid);
        ErrorResponseDto expectedErrorResponseDto = new ErrorResponseDto(errorMessage, ENTITY_NOT_FOUND);

        //when
        when(proceedingJoinPoint.proceed())
                .thenThrow(new EntityNotFoundException(notFoundUuid));
        when(entityNotFoundResponse.message())
                .thenReturn(errorMessage);
        when(entityNotFoundResponse.errorCode())
                .thenReturn(ENTITY_NOT_FOUND);
        when(entityNotFoundResponse.httpStatus())
                .thenReturn(statusCode);
        ResponseEntity<ErrorResponseDto> actual = (ResponseEntity<ErrorResponseDto>) entityNotFoundExceptionAspect
                .returnErrorDtoWhenCatchEntityNotFoundException(proceedingJoinPoint, entityNotFoundResponse);

        //then
       assertAll(
               () -> assertEquals(statusCode, actual.getStatusCode().value()),
               () -> assertEquals(expectedErrorResponseDto, actual.getBody())
       );
    }

    @Test
    void returnErrorDtoWhenCatchEntityNotFoundExceptionShouldReturnErrorDtoWithDefaultMessage() throws Throwable {
        //given
        int statusCode = 404;
        ErrorResponseDto expectedErrorResponseDto = new ErrorResponseDto(MESSAGE_DEFAULT, ENTITY_NOT_FOUND);

        //when
        when(proceedingJoinPoint.proceed())
                .thenThrow(new EntityNotFoundException());
        when(entityNotFoundResponse.errorCode())
                .thenReturn(ENTITY_NOT_FOUND);
        when(entityNotFoundResponse.message())
                .thenReturn("");
        when(entityNotFoundResponse.httpStatus())
                .thenReturn(statusCode);
        ResponseEntity<ErrorResponseDto> actual = (ResponseEntity<ErrorResponseDto>) entityNotFoundExceptionAspect
                .returnErrorDtoWhenCatchEntityNotFoundException(proceedingJoinPoint, entityNotFoundResponse);

        //then
       assertAll(
               () -> assertEquals(statusCode, actual.getStatusCode().value()),
               () -> assertEquals(expectedErrorResponseDto, actual.getBody())
       );
    }

    @Test
    void returnErrorDtoWhenCatchEntityNotFoundExceptionShouldReturnProcessObject() throws Throwable {
        //given
        UUID foundUuid = UUID.randomUUID();
        Dto processedReturningObject = new Dto(foundUuid);

        //when
        when(proceedingJoinPoint.proceed())
                .thenReturn(processedReturningObject);
        Dto actual = (Dto) entityNotFoundExceptionAspect.returnErrorDtoWhenCatchEntityNotFoundException(proceedingJoinPoint, entityNotFoundResponse);

        //then
        assertAll(
                () -> assertEquals(processedReturningObject, actual),
                () -> verify(entityNotFoundResponse, times(0)).errorCode(),
                () -> verify(entityNotFoundResponse, times(0)).message(),
                () -> verify(entityNotFoundResponse, times(0)).httpStatus()
        );
    }
}
