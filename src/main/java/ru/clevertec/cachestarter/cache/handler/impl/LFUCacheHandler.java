package ru.clevertec.cachestarter.cache.handler.impl;

import ru.clevertec.cachestarter.cache.handler.AlgorithmCacheHandler;
import ru.clevertec.cachestarter.cache.handler.CacheValue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс <code>LFUCacheHandler</code> кэширует объекты подсчитывает частоту использования каждого элемента
 * и удаляет те, к которым обращаются реже всего
 */
public class LFUCacheHandler<K, V> implements AlgorithmCacheHandler<K, V> {

    /**
     * K - id объекта, V - объект, CacheValue.key - частота использования элемента V
     */
    private final Map<K, CacheValue<Long, V>> cacheBase = new ConcurrentHashMap<>();

    private final int CACHE_CAPACITY;

    public LFUCacheHandler(int capacity) {
        this.CACHE_CAPACITY = capacity;
    }

    @Override
    public V put(K key, V value) {
        if (key == null || value == null) {
            return null;
        }
        CacheValue<Long, V> findCashValue = cacheBase.get(key);
        if (findCashValue == null || value != findCashValue.getValue()) {
            add(key, value);
        } else {
            increase(key);
        }
        return value;
    }

    @Override
    public V get(K key) {
        if (!cacheBase.containsKey(key)) {
            return null;
        }
        CacheValue<Long, V> cacheValue = cacheBase.get(key);
        increase(key);

        return cacheValue.getValue();
    }

    @Override
    public void remove(K key) {
        cacheBase.remove(key);
    }

    /**
     * Добавляет в кэш или обноввляет в нем новое значение
     *
     * @param key   id, кэшируемого, объекта, не null
     * @param value кэшируемый объект
     */
    private void add(K key, V value) {
        if (cacheBase.size() == CACHE_CAPACITY) {
            cacheBase.remove(minUseCacheObjectKey());
        }
        cacheBase.put(key, new CacheValue<>(1L, value));
    }

    /**
     * @return id минимально используемого объекта
     */
    private K minUseCacheObjectKey() {
        if (cacheBase.isEmpty()) {
            return null;
        }

        return cacheBase.entrySet().stream()
                .sorted((o1, o2) -> (int) (o1.getValue().getKey() - o2.getValue().getKey()))
                .toList()
                .get(0)
                .getKey();
    }

    /**
     * Увеличивает частоту использований кэшируемого объекта
     *
     * @param key id кэшируемого объекта
     */
    private void increase(K key) {
        CacheValue<Long, V> cacheValue = cacheBase.get(key);
        cacheValue.setKey(cacheValue.getKey() + 1);
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
