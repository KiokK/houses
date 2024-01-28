package ru.clevertec.houses.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.TestConstructor;
import ru.clevertec.cachestarter.cache.handler.AlgorithmCacheHandler;
import ru.clevertec.houses.dao.HouseDao;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.service.HouseService;
import ru.clevertec.houses.service.PersonService;
import ru.clevertec.houses.util.PostgresqlContainerInitializer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.clevertec.houses.util.dto.HouseDtoTestData.getListHouseDto;
import static ru.clevertec.houses.util.dto.PersonDtoTestData.getListPersonDto;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ServicesCachingIntegrationTest extends PostgresqlContainerInitializer {

    private final HouseDao houseRepository;

    private final PersonService personService;
    private final HouseService houseService;

    @SpyBean
    private AlgorithmCacheHandler<Object, Object> cacheHandler;

    @Test
    void checkCreationPersonsAndHousesWithConcurrentCache() throws ExecutionException, InterruptedException {
        //given
        List<PersonDto> personDtos = getListPersonDto();
        List<HouseDto> houseDtos = getListHouseDto();
        int expectedPersonsCount = personDtos.size();
        int expectedHousesCount = houseDtos.size();
        int expectedPullCallMethod = 12;

        //when
        ExecutorService serviceHouses = Executors.newFixedThreadPool(6);
        for (int i = 0; i < houseDtos.size(); i++) {
            final HouseDto dto = houseDtos.get(i);
            Future<HouseDto> ans = serviceHouses.submit(
                    () -> houseService.create(dto)
            );
            houseDtos.set(i, ans.get());
            personDtos.get(i).houseLiveUuid = houseDtos.get(i).uuid;
        }

        ExecutorService servicePersons = Executors.newFixedThreadPool(6);
        List<Future<PersonDto>> featurePersons = new ArrayList<>();
        for (final PersonDto dto : personDtos) {
            featurePersons.add(servicePersons.submit(
                    () -> personService.create(dto)
            ));
        }
        for (Future<PersonDto> feature : featurePersons) {
            personService.deleteByUuid(feature.get().uuid);
        }

        //then
        assertAll(
                () -> assertEquals(expectedHousesCount, houseRepository.count()),
                () -> verify(cacheHandler, times(expectedPullCallMethod)).put(any(), any()),
                () -> verify(cacheHandler, times(expectedPersonsCount)).remove(any(UUID.class))
        );
    }

}
