package ru.clevertec.houses.integration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.cachestarter.cache.handler.AlgorithmCacheHandler;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.service.HouseService;
import ru.clevertec.houses.service.PersonService;

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
import static ru.clevertec.houses.util.HouseDtoTestData.getListHouseDto;
import static ru.clevertec.houses.util.PersonDtoTestData.getListPersonDtoSize6;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class IntegrationServicesCachingTest {

    private final PersonService personService;
    private final HouseService houseService;

    @SpyBean
    private AlgorithmCacheHandler<Object, Object> cacheHandler;

    @Test
    void checkCreationPersonsAndHousesWithConcurrentCache() throws ExecutionException, InterruptedException {
        //given
        List<PersonDto> personDtos = getListPersonDtoSize6();
        List<HouseDto> houseDtos = getListHouseDto();
        int expectedPutHouseCount = houseDtos.size();
        int expectedPutPersonsCount = personDtos.size();
        int expectedRemovePersonsCount = personDtos.size();

        //when
        ExecutorService serviceHouses = Executors.newFixedThreadPool(6);
        for (int i = 0; i < houseDtos.size(); i++) {
            final HouseDto dto = houseDtos.get(i);
            Future<HouseDto> createdHouseDto = serviceHouses.submit(
                    () -> houseService.create(dto)
            );
            houseDtos.set(i, createdHouseDto.get());
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
                () -> verify(cacheHandler, times(expectedPutHouseCount)).put(any(UUID.class), any(PersonDto.class)),
                () -> verify(cacheHandler, times(expectedPutPersonsCount)).put(any(UUID.class), any(HouseDto.class)),
                () -> verify(cacheHandler, times(expectedRemovePersonsCount)).remove(any(UUID.class))
        );
    }

}
