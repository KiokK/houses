package ru.clevertec.cachestarter.exeption;

public class CacheInitializationException extends RuntimeException {

    public CacheInitializationException(String property) {
        super(String.format("Error cash-starter application.property: '%s'", property));
    }

}
