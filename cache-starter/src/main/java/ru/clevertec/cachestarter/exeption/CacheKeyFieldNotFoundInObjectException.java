package ru.clevertec.cachestarter.exeption;

public class CacheKeyFieldNotFoundInObjectException extends NoSuchFieldException {

    public CacheKeyFieldNotFoundInObjectException(String fieldName) {
        super(String.format("Cacheable object has not field '%s'", fieldName));
    }

}
