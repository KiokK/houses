package ru.clevertec.exceptionhandlerstarter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ru.clevertec.exceptionhandlerstarter.domain.ErrorCodeConstants.ENTITY_NOT_FOUND;

/**
 * Annotated class catching {@link ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException}.
 * By default, using message from EntityNotFoundException.
 *
 * @author kiok
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityNotFoundResponse {

    int errorCode() default ENTITY_NOT_FOUND;
    int httpStatus() default 404;
    String message() default "";

}
