package ru.clevertec.cachestarter.cache.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Первый передаваемый в метод параметр соответствует ключу по которому будет искаться объект в кэше
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GetFromCache {
}
