package ru.clevertec.cachestarter.cache.handler;

import java.util.Map;

/**
 * интерфейс, который реализуют обработчики кэша
 *
 * @param <K> ключ для поиска данных в кэше, id объекта, не null
 * @param <V> объект, который кэшируется
 */
public interface AlgorithmCacheHandler<K, V> {

    /**
     * Вставка объекта в кэш
     *
     * @param key   id объекта, не null
     * @param value объект, который кэшируем
     * @return <code>value</code>
     */
    V put(K key, V value);

    /**
     * Получение объекта из кэша
     *
     * @param key id объекта, не null
     * @return объект из кэша
     */
    V get(K key);

    /**
     * Удаление объекта из кэша по ключу
     *
     * @param key id кэшируемого объекта, не null
     */
    void remove(K key);

    void clean();

    Map getCopyOfCacheData();
}
