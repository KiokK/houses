package ru.clevertec.cachestarter.cache.handler.impl;

import ru.clevertec.cachestarter.cache.handler.AlgorithmCacheHandler;
import ru.clevertec.cachestarter.cache.handler.CacheValue;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Класс <code>LRUCacheHandler</code> кэширует объекты подсчитывает частоту использования каждого элемента.
 * Элемент, который не использовался в течение самого длительного времени, будет удален из кэша.
 */
public class LRUCacheHandler<K, V> implements AlgorithmCacheHandler<K, V> {

    private final int CACHE_SIZE;

    private final LinkedHashMap<K, CacheValue<K, V>> cacheBase = new LinkedHashMap<>();

    public LRUCacheHandler(int capacity) {
        CACHE_SIZE = capacity;
    }

    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            return null;
        }
        if (cacheBase.size() + 1 > CACHE_SIZE) {
            cacheBase.remove(getKeyOfFirst());
        }
        cacheBase.put(key, new CacheValue<>(key, value));
        return value;
    }

    @Override
    public V get(K key) {
        CacheValue<K, V> findValue = cacheBase.get(key);
        if (findValue == null) {
            return null;
        }
        remove(key);
        cacheBase.put(key, findValue);

        return findValue.getValue();
    }

    @Override
    public void remove(K key) {
        cacheBase.remove(key);
    }

    /**
     * Возвращает ключ элемента, который не использовался в течение самого длительного времени
     * @return ключ кэшируемого объекта
     */
    private K getKeyOfFirst(){
        return cacheBase.entrySet()
                .iterator()
                .next()
                .getKey();
    }

    /**
     * @return Map копия объектов кэша, где CacheValue.key (Long) - частота использования элемента
     */
    @Override
    public Map getCopyOfCacheData() {
        return Map.copyOf(cacheBase);
    }

    /**
     * Удаление всех данных из кэша
     */
    @Override
    public void clean() {
        cacheBase.clear();
    }

}
