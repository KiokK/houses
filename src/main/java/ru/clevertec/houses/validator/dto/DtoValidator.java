package ru.clevertec.houses.validator.dto;

import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public interface DtoValidator<T> {

    boolean isValid(@NotNull T dto) throws ValidationException;

    boolean isValidWithNotNullUuid(T dto) throws ValidationException;

    default void isNotNull(UUID uuid) throws ValidationException {
        if (uuid == null) {
            throw new ValidationException();
        }
    }

}
