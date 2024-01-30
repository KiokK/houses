package ru.clevertec.cachestarter.cache.handler.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.clevertec.cachestarter.cache.handler.CacheValue;
import ru.clevertec.cachestarter.util.UserDto;
import ru.clevertec.cachestarter.util.UserDtoTestData;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LFUCacheHandlerTest {

    private LFUCacheHandler<UUID, UserDto> cacheHandler;

    @BeforeEach
    void setUp() {
        final int CAPACITY = 2;
        cacheHandler = new LFUCacheHandler<>(CAPACITY);
    }

    @Nested
    class Put {

        @Test
        void putObjectsShouldAutoDeleteTheLessUsed() {
            //given
            final int EXPECTED_SIZE = 2;
            UserDto firstDto = UserDtoTestData.getUserDto1();
            UserDto secondDto = UserDtoTestData.getUserDto2();
            UserDto thirdDto = UserDtoTestData.getUserDto3();

            //when
            cacheHandler.put(firstDto.id, firstDto);
            cacheHandler.put(firstDto.id, firstDto);
            cacheHandler.put(firstDto.id, firstDto);
            cacheHandler.put(secondDto.id, secondDto);
            cacheHandler.put(thirdDto.id, thirdDto);
            Map<UUID, CacheValue<Long, UserDto>> actualCacheBase = cacheHandler.getCopyOfCacheData();

            //then
            assertAll(
                    () -> assertThat(actualCacheBase.size()).isEqualTo(EXPECTED_SIZE),
                    () -> assertThat(actualCacheBase.get(firstDto.id).getValue()).isEqualTo(firstDto),
                    () -> assertThat(actualCacheBase.get(thirdDto.id).getValue()).isEqualTo(thirdDto)
            );
        }

    }

    @Nested
    class Get {

        @Test
        void getCheckCountOfUsage() {
            //given
            final int EXPECTED_SIZE = 2;
            final int EXPECTED_FIRST_DTO_COUNT = 3;
            final int EXPECTED_SECOND_DTO_COUNT = 1;
            UserDto firstDto = UserDtoTestData.getUserDto1();
            UserDto secondDto = UserDtoTestData.getUserDto2();

            //when
            cacheHandler.put(firstDto.id, firstDto);
            cacheHandler.get(firstDto.id);
            cacheHandler.get(firstDto.id);
            cacheHandler.put(secondDto.id, secondDto);
            Map<UUID, CacheValue<Long, UserDto>> actualCacheBase = cacheHandler.getCopyOfCacheData();

            //then
            assertAll(
                    () -> assertThat(actualCacheBase.size()).isEqualTo(EXPECTED_SIZE),
                    () -> assertThat(actualCacheBase.get(firstDto.id).getKey()).isEqualTo(EXPECTED_FIRST_DTO_COUNT),
                    () -> assertThat(actualCacheBase.get(firstDto.id).getValue()).isEqualTo(firstDto),
                    () -> assertThat(actualCacheBase.get(secondDto.id).getKey()).isEqualTo(EXPECTED_SECOND_DTO_COUNT),
                    () -> assertThat(actualCacheBase.get(secondDto.id).getValue()).isEqualTo(secondDto)
            );
        }

    }

    @Nested
    class Remove {

        @Test
        void removeShouldRemoveObject() {
            //given
            final int EXPECTED_SIZE = 1;
            UserDto firstDto = UserDtoTestData.getUserDto1();
            UserDto secondDto = UserDtoTestData.getUserDto2();

            //when
            cacheHandler.put(firstDto.id, firstDto);
            cacheHandler.put(secondDto.id, secondDto);
            cacheHandler.remove(firstDto.id);
            Map<UUID, CacheValue<Long, UserDto>> actualCacheBase = cacheHandler.getCopyOfCacheData();

            //then
            assertAll(
                    () -> assertThat(actualCacheBase.size()).isEqualTo(EXPECTED_SIZE),
                    () -> assertThat(actualCacheBase.get(secondDto.id).getValue()).isEqualTo(secondDto),
                    () -> assertThat(actualCacheBase.get(firstDto.id)).isNull()
            );
        }

    }

    @Nested
    class GetCopyOfCacheData {

        @Test
        void getCopyOfCacheData() {
            //given
            UserDto firstDto = UserDtoTestData.getUserDto1();
            UserDto secondDto = UserDtoTestData.getUserDto2();
            Map<UUID, CacheValue<Long, UserDto>> expected = Map.of(
                    firstDto.id, new CacheValue<>(2L, firstDto),
                    secondDto.id, new CacheValue<>(1L, secondDto)
            );

            //when
            cacheHandler.put(firstDto.id, firstDto);
            cacheHandler.put(firstDto.id, firstDto);
            cacheHandler.put(secondDto.id, secondDto);
            Map<UUID, CacheValue<Long, UserDto>> actualCacheBase = cacheHandler.getCopyOfCacheData();

            //then
            assertThat(actualCacheBase)
                    .isEqualTo(expected);
        }

    }

    @Nested
    class Clean {

        @Test
        void cleanShouldCleanAfterPut() {
            //given
            final int EXPECTED_SIZE = 0;
            UserDto firstDto = UserDtoTestData.getUserDto1();

            //when
            cacheHandler.put(firstDto.id, firstDto);
            cacheHandler.remove(firstDto.id);
            cacheHandler.clean();
            Map<UUID, CacheValue<Long, UserDto>> actualCacheBase = cacheHandler.getCopyOfCacheData();

            //then
            assertAll(
                    () -> assertThat(actualCacheBase).isNotNull(),
                    () -> assertThat(actualCacheBase).hasSize(EXPECTED_SIZE)
            );
        }

    }

}
