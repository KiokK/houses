package ru.clevertec.houses.validator.dto;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.houses.dto.HouseDto;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.clevertec.houses.util.ConstantsTest.EMPTY_STRING;
import static ru.clevertec.houses.util.HouseDtoTestData.validHouseDtoBuilder;

class HouseDtoValidatorTest {

    private final HouseDtoValidator validator = new HouseDtoValidator();

    @Nested
    class IsValid {

        @Test
        void checkIsValidShouldReturnTrue() {
            //given
            HouseDto dto = validHouseDtoBuilder().build();

            //when//then
            Assertions.assertTrue(validator.isValid(dto));
        }

        @ParameterizedTest
        @MethodSource("argsForValidationException")
        void checkIsValidShouldThrowsValidationException(HouseDto argDto) {
            assertThrows(ValidationException.class,
                    () -> validator.isValid(argDto));
        }

        static Stream<HouseDto> argsForValidationException() {
            return Stream.of(
                    validHouseDtoBuilder()
                            .street(EMPTY_STRING).build(),
                    validHouseDtoBuilder()
                            .country(EMPTY_STRING).build(),
                    validHouseDtoBuilder()
                            .city(EMPTY_STRING).build(),
                    validHouseDtoBuilder()
                            .number(null).build(),
                    validHouseDtoBuilder()
                            .country(null).build(),
                    validHouseDtoBuilder()
                            .area(null).build(),
                    validHouseDtoBuilder()
                            .city(null).build(),
                    validHouseDtoBuilder()
                            .street(null).build(),
                    null);
        }

    }

    @Nested
    class IsValidWithNotNullUuid {

        @Test
        void checkIsValidWithNotNullUuid() {
            //given
            HouseDto noValidDto = validHouseDtoBuilder().uuid(null).build();

            //when//then
            assertThrows(ValidationException.class,
                    () -> validator.isValidWithNotNullUuid(noValidDto));
        }
    }

}
