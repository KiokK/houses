package ru.clevertec.cachestarter.cache.proxy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.cachestarter.cache.CacheFactory;
import ru.clevertec.cachestarter.cache.handler.AlgorithmCacheHandler;
import ru.clevertec.cachestarter.util.UserDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.clevertec.cachestarter.util.UserDtoTestData.getUserDto1;

@ExtendWith(MockitoExtension.class)
class CacheAspectTest {

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @Mock
    private AlgorithmCacheHandler<Object, Object> cacheHandler;

    @Mock
    private CacheFactory cacheFactory;

    @InjectMocks
    private CacheAspect cacheAspect;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        cacheFactory = new CacheFactory(cacheHandler, "id");
        cacheAspect = new CacheAspect(cacheFactory);
    }

    @Nested
    class PutCache {

        @Test
        void putCacheShouldReturnExpectedDto() throws Throwable {
            //given
            UserDto expected = getUserDto1();

            // when
            when(proceedingJoinPoint.proceed()).thenReturn(expected);
            Object actual = cacheAspect.putCache(proceedingJoinPoint);

            //then
            assertAll(
                    () -> verify(cacheHandler).put(expected.id, expected),
                    () -> assertThat(actual).isEqualTo(expected)
            );
        }

    }

    @Nested
    class GetCache {

        @Test
        void getCacheShouldReturnCacheWhenExists() throws Throwable {
            //given
            UserDto userDto = getUserDto1();
            UserDto expected = getUserDto1();
            final Object[] ARGS = new Object[]{userDto.id};

            // when
            when(proceedingJoinPoint.getArgs()).thenReturn(ARGS);
            when(cacheHandler.get(userDto.id)).thenReturn(userDto);
            Object actual = cacheAspect.getCache(proceedingJoinPoint);

            //then
            assertAll(
                    () -> verify(proceedingJoinPoint, times(0)).proceed(),
                    () -> assertThat(actual).isEqualTo(expected)
            );
        }

        @Test
        void getCacheShouldReturnObjectAndAddToCache() throws Throwable {
            //given
            UserDto userDto = getUserDto1();
            UserDto expected = getUserDto1();
            final Object[] ARGS = new Object[]{userDto.id};

            // when
            when(proceedingJoinPoint.getArgs()).thenReturn(ARGS);
            when(cacheHandler.get(userDto.id)).thenReturn(null);
            when(proceedingJoinPoint.proceed()).thenReturn(userDto);
            Object actual = cacheAspect.getCache(proceedingJoinPoint);

            //then
            assertThat(actual)
                    .isEqualTo(expected);
        }

    }

    @Nested
    class PostCache {

        @Test
        void postCacheShouldReturnDtoAfterPut() throws Throwable {
            //given
            UserDto expected = getUserDto1();

            //when
            when(proceedingJoinPoint.proceed()).thenReturn(expected);
            Object actual = cacheAspect.postCache(proceedingJoinPoint);

            //then
            assertAll(
                    () -> verify(cacheHandler).put(expected.id, expected),
                    () -> assertThat(actual).isEqualTo(expected)
            );
        }

    }

    @Nested
    class DeleteCache {

        @Test
        void deleteCacheVerifySteps() throws Throwable {
            //given
            UserDto userDto = getUserDto1();
            final boolean SERVICE_PROCESS_ANSWER = true;
            final Object[] ARGS = new Object[]{userDto.id};

            // when
            when(proceedingJoinPoint.getArgs()).thenReturn(ARGS);
            when(proceedingJoinPoint.proceed()).thenReturn(SERVICE_PROCESS_ANSWER);
            cacheAspect.deleteCache(proceedingJoinPoint);

            //then
            verify(cacheHandler).remove(userDto.id);
        }

    }
}
