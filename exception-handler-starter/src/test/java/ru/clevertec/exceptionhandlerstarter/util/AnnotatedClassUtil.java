package ru.clevertec.exceptionhandlerstarter.util;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.clevertec.exceptionhandlerstarter.annotation.EntityNotFoundResponse;
import ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException;

import java.util.UUID;

import static ru.clevertec.exceptionhandlerstarter.util.Constants.TEST_ERROR_CODE;
import static ru.clevertec.exceptionhandlerstarter.util.Constants.TEST_HTTP_STATUS;

@Component
@EntityNotFoundResponse(errorCode = TEST_ERROR_CODE, httpStatus = TEST_HTTP_STATUS)
public class AnnotatedClassUtil {

    public ResponseEntity<?> method(UUID uuid, boolean isThrowing) throws EntityNotFoundException {
        if (!isThrowing) {
            return ResponseEntity.ok(new Dto(uuid));
        }

        throw new EntityNotFoundException(uuid);
    }

}
