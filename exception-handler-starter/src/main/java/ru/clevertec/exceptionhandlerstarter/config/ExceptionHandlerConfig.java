package ru.clevertec.exceptionhandlerstarter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.exceptionhandlerstarter.aspect.EntityNotFoundExceptionAspect;

@Configuration
public class ExceptionHandlerConfig {

    @Bean
    public EntityNotFoundExceptionAspect entityNotFoundExceptionAspect() {
        return new EntityNotFoundExceptionAspect();
    }

}
