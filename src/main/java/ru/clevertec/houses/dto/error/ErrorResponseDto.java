package ru.clevertec.houses.dto.error;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class ErrorResponseDto {

    public String errorMessage;

    public int errorCode;

}
