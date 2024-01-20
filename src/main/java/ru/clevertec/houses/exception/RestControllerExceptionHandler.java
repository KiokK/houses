package ru.clevertec.houses.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.houses.dto.error.ErrorResponseDto;

import java.util.HashMap;
import java.util.Map;

import static ru.clevertec.houses.dto.error.ErrorCodeConstants.ENTITY_NOT_FOUND;
import static ru.clevertec.houses.dto.error.ErrorCodeConstants.REQUEST_NOT_VALID;

@RestControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(e.getMessage(), ENTITY_NOT_FOUND));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(errors.toString(), REQUEST_NOT_VALID));
    }

}
