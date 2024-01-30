package ru.clevertec.cachestarter.cache.proxy;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import ru.clevertec.cachestarter.cache.CacheFactory;
import ru.clevertec.cachestarter.exeption.CacheKeyFieldNotFoundInObjectException;

import java.lang.reflect.Field;

/**
 * Kласс обработки поиска данных в кэше перед взаимодействием и(или) после взаимодействия с БД.
 * Методы используют рефлексию для получения данных к уникальному полю объекта, определенного в
 * {@link CacheFactory#getIdFieldName()}
 */
@Aspect
@RequiredArgsConstructor
public class CacheAspect {

    private final CacheFactory cacheFactory;

    /**
     * Вставка данных в кэш: берет результат выполнения аннотируемого метода и вставляет в кэш
     *
     * @param joinPoint вход в метод
     * @param <K>       уникальный id кэшируемого значения
     * @param <V>       кэшируемое значение
     * @return объект из dao, который так же уже присутствует в кэше
     * @throws Throwable                              ошибка доступа к id объекта по полю {@link CacheFactory#getIdFieldName()}
     *                                                или если процесс выбросит исключение
     * @throws CacheKeyFieldNotFoundInObjectException если у объекта из кэша нет поля соответствующего {@link CacheFactory#getIdFieldName()}
     */
    @Around("@annotation(PutToCache)")
    public <K, V> Object putCache(ProceedingJoinPoint joinPoint) throws CacheKeyFieldNotFoundInObjectException, Throwable {
        V serviceDataResult = (V) joinPoint.proceed();
        Field idField;
        try {
            idField = serviceDataResult.getClass().getDeclaredField(cacheFactory.getIdFieldName());
        } catch (NoSuchFieldException e) {

            throw new CacheKeyFieldNotFoundInObjectException(cacheFactory.getIdFieldName());
        }
        idField.setAccessible(true);
        K id = (K) idField.get(serviceDataResult);
        cacheFactory.getCacheHandler().put(id, serviceDataResult);

        return serviceDataResult;
    }

    /**
     * Находит данные в кэше, если не находит, тогда берет результат выполнения аннотируемого метода и вставляет в кэш
     *
     * @param joinPoint вход в метод
     * @param <K>       уникальный id кэшируемого значения
     * @param <V>       кэшируемое значение
     * @return объект из dao, который так же уже присутствует в кэше
     * @throws Throwable если процесс выбросит исключение
     */
    @Around("@annotation(GetFromCache)")
    public <K, V> Object getCache(ProceedingJoinPoint joinPoint) throws Throwable {
        K idFromService = (K) joinPoint.getArgs()[0];
        V valueFromCache = (V) cacheFactory.getCacheHandler().get(idFromService);
        if (valueFromCache != null) {

            return valueFromCache;
        }
        V serviceResult = (V) joinPoint.proceed();
        if (serviceResult != null) {
            cacheFactory.getCacheHandler().put(idFromService, serviceResult);
        }

        return serviceResult;
    }

    /**
     * Выполняется аннотируемый метод, а потом в кэш вставляется результат
     *
     * @param joinPoint вход в метод
     * @param <K>       уникальный id кэшируемого значения
     * @param <V>       кэшируемое значение
     * @return объект из dao, который так же уже присутствует в кэше
     * @throws Throwable                              ошибка доступа к id объекта по полю {@link CacheFactory#getIdFieldName()}
     *                                                или если процесс выбросит исключение
     * @throws CacheKeyFieldNotFoundInObjectException если у объекта из кэша нет поля соответствующего {@link CacheFactory#getIdFieldName()}
     */
    @Around("@annotation(PostFromCache)")
    public <K, V> Object postCache(ProceedingJoinPoint joinPoint) throws Throwable {
        V processDataResult = (V) joinPoint.proceed();
        Field idField;
        try {
            idField = processDataResult.getClass().getDeclaredField(cacheFactory.getIdFieldName());
        } catch (NoSuchFieldException e) {

            throw new CacheKeyFieldNotFoundInObjectException(cacheFactory.getIdFieldName());
        }
        idField.setAccessible(true);
        K id = (K) idField.get(processDataResult);
        cacheFactory.getCacheHandler().put(id, processDataResult);

        return processDataResult;
    }

    /**
     * Выполняется аннотируемый метод, а потом удаляет данные из кэша
     *
     * @param joinPoint вход в метод
     * @param <K>       уникальный id кэшируемого значения
     * @param <V>       кэшируемое значение
     * @throws Throwable если процесс выбросит исключение
     */
    @Around("@annotation(DeleteFromCache)")
    public <K, V> Object deleteCache(ProceedingJoinPoint joinPoint) throws Throwable {
        V serviceDataResult = (V) joinPoint.proceed();
        K id = (K) joinPoint.getArgs()[0];
        joinPoint.proceed();
        cacheFactory.getCacheHandler().remove(id);

        return serviceDataResult;
    }

}
