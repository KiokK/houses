package ru.clevertec.exceptionhandlerstarter.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@AllArgsConstructor
public class ErrorResponseDto {

    public String errorMessage;
    public int errorCode;

}
