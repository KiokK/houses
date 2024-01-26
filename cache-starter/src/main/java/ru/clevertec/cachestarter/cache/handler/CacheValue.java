package ru.clevertec.cachestarter.cache.handler;

import lombok.Data;

import java.util.Objects;

/**
 * Типизированный класс <code>CashValue</code> хранит в себе значение кэшированного объекта
 * используется в классах реализующих {@link AlgorithmCacheHandler}
 *
 * @param <K> id объекта, который кэшируем
 * @param <V> объект, который кэшируется
 */
@Data
public class CacheValue<K, V> {

    private K key;

    private V value;

    public CacheValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CacheValue<?, ?> cacheValue = (CacheValue<?, ?>) o;

        return Objects.equals(key, cacheValue.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

}
