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

class LRUCacheHandlerTest {

    private LRUCacheHandler<UUID, UserDto> cacheHandler;

    @BeforeEach
    void setUp() {
        final int CAPACITY = 2;
        cacheHandler = new LRUCacheHandler<>(CAPACITY);
    }

    @Nested
    class Put {

        @Test
        void putThreeObjectsShouldDeleteTheOldest() {
            //given
            final int EXPECTED_SIZE = 2;
            UserDto firstDto = UserDtoTestData.getUserDto1();
            UserDto secondDto = UserDtoTestData.getUserDto2();
            UserDto thirdDto = UserDtoTestData.getUserDto3();

            //when
            cacheHandler.put(firstDto.id, firstDto);
            cacheHandler.put(secondDto.id, secondDto);
            cacheHandler.put(thirdDto.id, thirdDto);
            Map<UUID, CacheValue<UUID, UserDto>> actualCacheBase = cacheHandler.getCopyOfCacheData();

            //then
            assertAll(
                    () -> assertThat(actualCacheBase.size()).isEqualTo(EXPECTED_SIZE),
                    () -> assertThat(actualCacheBase.get(secondDto.id).getValue()).isEqualTo(secondDto),
                    () -> assertThat(actualCacheBase.get(thirdDto.id).getValue()).isEqualTo(thirdDto)
            );
        }

        @Test
        void putTwoEqualsObjectShouldPutOne() {
            //given
            final int EXPECTED_SIZE = 1;
            UserDto testDto = UserDtoTestData.getUserDto1();

            //when
            cacheHandler.put(testDto.id, testDto);
            cacheHandler.put(testDto.id, testDto);
            Map<UUID, CacheValue<UUID, UserDto>> actualCacheBase = cacheHandler.getCopyOfCacheData();

            //then
            assertAll(
                    () -> assertThat(actualCacheBase.size()).isEqualTo(EXPECTED_SIZE),
                    () -> assertThat(actualCacheBase.get(testDto.id).getValue()).isEqualTo(testDto)
            );
        }

    }

    @Nested
    class Get {

        @Test
        void getShouldSwapOrderOfElements() {
            //given
            final int EXPECTED_SIZE = 2;
            UserDto firstDto = UserDtoTestData.getUserDto1();
            UserDto secondDto = UserDtoTestData.getUserDto2();
            UserDto thirdDto = UserDtoTestData.getUserDto3();

            //when
            cacheHandler.put(firstDto.id, firstDto);
            cacheHandler.put(secondDto.id, secondDto);
            UserDto actualValueByFirstId = cacheHandler.get(firstDto.id);
            cacheHandler.put(thirdDto.id, thirdDto);
            UserDto actualValueByThirdId = cacheHandler.get(thirdDto.id);
            Map<UUID, CacheValue<UUID, UserDto>> actualCacheBase = cacheHandler.getCopyOfCacheData();

            //then
            assertAll(
                    () -> assertThat(actualCacheBase.size()).isEqualTo(EXPECTED_SIZE),
                    () -> assertThat(firstDto).isEqualTo(actualValueByFirstId),
                    () -> assertThat(thirdDto).isEqualTo(actualValueByThirdId)
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
            Map<UUID, CacheValue<UUID, UserDto>> actualCacheBase = cacheHandler.getCopyOfCacheData();

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
            Map<UUID, CacheValue<UUID, UserDto>> expected = Map.of(
                    firstDto.id, new CacheValue<>(firstDto.id, firstDto),
                    secondDto.id, new CacheValue<>(secondDto.id, secondDto)
            );

            //when
            cacheHandler.put(firstDto.id, firstDto);
            cacheHandler.put(secondDto.id, secondDto);
            Map<UUID, CacheValue<UUID, UserDto>> actualCacheBase = cacheHandler.getCopyOfCacheData();

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
            Map<UUID, CacheValue<UUID, UserDto>> actualCacheBase = cacheHandler.getCopyOfCacheData();

            //then
            assertAll(
                    () -> assertThat(actualCacheBase).isNotNull(),
                    () -> assertThat(actualCacheBase).hasSize(EXPECTED_SIZE)
            );
        }
    }

}
