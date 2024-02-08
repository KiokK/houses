package ru.clevertec.cachestarter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.cachestarter.cache.CacheFactory;
import ru.clevertec.cachestarter.cache.handler.AlgorithmCacheHandler;
import ru.clevertec.cachestarter.cache.handler.impl.LFUCacheHandler;
import ru.clevertec.cachestarter.cache.handler.impl.LRUCacheHandler;
import ru.clevertec.cachestarter.cache.proxy.CacheAspect;
import ru.clevertec.cachestarter.exeption.CacheInitializationException;

@Configuration
@ConditionalOnProperty(value = {"cache.algorithm-type", "cache.capacity", "cache.key-field-name"})
public class CacheConfig {

    @Value("${cache.algorithm-type}")
    private String algorithmType;

    @Value("${cache.capacity}")
    private int capacity;

    @Value("${cache.key-field-name}")
    private String keyFieldName;

    @Bean
    public AlgorithmCacheHandler<Object, Object> cacheHandler() throws CacheInitializationException {
        TypeOfCacheAlgorithm typeOfCacheAlgorithm;
        try {
            typeOfCacheAlgorithm = TypeOfCacheAlgorithm.valueOf(algorithmType);
        } catch (IllegalArgumentException e) {
            throw new CacheInitializationException(algorithmType);
        }

        return switch (typeOfCacheAlgorithm) {
            case LFU -> new LFUCacheHandler<>(capacity);
            case LRU -> new LRUCacheHandler<>(capacity);
        };
    }

    @ConditionalOnMissingBean
    @Bean
    public CacheFactory cacheFactory(AlgorithmCacheHandler<Object, Object> cacheHandler) {

        return new CacheFactory(cacheHandler, keyFieldName);
    }

    @ConditionalOnMissingBean
    @Bean
    public CacheAspect cacheAspect(CacheFactory cacheFactory) {

        return new CacheAspect(cacheFactory);
    }

    private enum TypeOfCacheAlgorithm {
        LFU, LRU
    }

}
