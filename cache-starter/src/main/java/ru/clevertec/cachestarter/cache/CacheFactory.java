package ru.clevertec.cachestarter.cache;

import ru.clevertec.cachestarter.cache.handler.AlgorithmCacheHandler;

/**
 * Фабрика возвращающая инициализированный обработчик кэша
 */
public class CacheFactory {

    /**
     * имя поля, по которому будут кэшироваться объекты
     */
    private final String ID;
    /**
     * Обработчик кэша
     */

    private final AlgorithmCacheHandler cacheHandler;

    /**
     * @return Обработчик кэша
     */
    public AlgorithmCacheHandler getCacheHandler() {
        return cacheHandler;
    }

    public CacheFactory(AlgorithmCacheHandler cacheHandler, String id) {
        ID = id;
        this.cacheHandler = cacheHandler;
    }

    /**
     * @return имя поля, по которому будут кэшироваться объекты
     */
    public String getIdFieldName() {
        return ID;
    }

}
