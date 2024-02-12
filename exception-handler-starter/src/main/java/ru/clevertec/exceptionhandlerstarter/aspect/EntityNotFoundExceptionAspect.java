package ru.clevertec.exceptionhandlerstarter.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import ru.clevertec.exceptionhandlerstarter.annotation.EntityNotFoundResponse;
import ru.clevertec.exceptionhandlerstarter.domain.ErrorResponseDto;
import ru.clevertec.exceptionhandlerstarter.exception.EntityNotFoundException;

@Aspect
public class EntityNotFoundExceptionAspect {

    @Around(value = "execution(* *(..)) && @within(annotationData)",
            argNames = "joinPoint,annotationData")
    public Object returnErrorDtoWhenCatchEntityNotFoundException(final ProceedingJoinPoint joinPoint, final EntityNotFoundResponse annotationData) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (EntityNotFoundException e) {
            if (!annotationData.message().isBlank()) {
                return ResponseEntity.status(annotationData.httpStatus())
                        .body(new ErrorResponseDto(annotationData.message(), annotationData.errorCode()));
            }

            return ResponseEntity.status(annotationData.httpStatus())
                    .body(new ErrorResponseDto(e.getMessage(), annotationData.errorCode()));
        }
    }

}
