# Custom cache-starter with spring-boot

- [LFUCacheHandler.java](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fcachestarter%2Fcache%2Fhandler%2Fimpl%2FLFUCacheHandler.java)
- [LRUCacheHandler.java](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fcachestarter%2Fcache%2Fhandler%2Fimpl%2FLRUCacheHandler.java)
- Beans - [CacheConfig.java](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fcachestarter%2Fconfig%2FCacheConfig.java)
- [cache/proxy/](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fcachestarter%2Fcache%2Fproxy)
  - AOP - [CacheAspect.java](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fcachestarter%2Fcache%2Fproxy%2FCacheAspect.java)
    - [@DeleteFromCache](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fcachestarter%2Fcache%2Fproxy%2FDeleteFromCache.java)
    - [@GetFromCache](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fcachestarter%2Fcache%2Fproxy%2FGetFromCache.java)
    - [@PostFromCache](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fcachestarter%2Fcache%2Fproxy%2FPostFromCache.java)
    - [@PutToCache](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fcachestarter%2Fcache%2Fproxy%2FPutToCache.java)
- Exceptions:
  - [CacheInitializationException.java](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fcachestarter%2Fexeption%2FCacheInitializationException.java)
  - [CacheKeyFieldNotFoundInObjectException.java](src%2Fmain%2Fjava%2Fru%2Fclevertec%2Fcachestarter%2Fexeption%2FCacheKeyFieldNotFoundInObjectException.java)
- properties - [META-INF/spring/additional-spring-configuration-metadata.json](src%2Fmain%2Fresources%2FMETA-INF%2Fadditional-spring-configuration-metadata.json)

### Publish to mavenLocal

```groovy
./gradlew publishToMavenLocal
```

### Add to project: 

build.gradle
```groovy
implementation 'ru.clevertec:cache-starter:1.0.0'
```

application.yaml
```yaml
cache:
  algorithm-type: LFU
  capacity: 10
  key-field-name: uuid
```

### Spring-Boot docs - useful link
https://docs.spring.io/spring-boot/docs/3.2.1/reference/html/configuration-metadata.html#appendix.configuration-metadata.annotation-processor
