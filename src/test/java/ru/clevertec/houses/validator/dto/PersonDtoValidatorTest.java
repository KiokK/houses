package ru.clevertec.houses.validator.dto;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.MethodSource;
import ru.clevertec.houses.dto.PersonDto;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.clevertec.houses.util.ConstantsTest.EMPTY_STRING;
import static ru.clevertec.houses.util.ConstantsTest.INCORRECT_PASSPORT_NUMBER_LONG;
import static ru.clevertec.houses.util.ConstantsTest.INCORRECT_PASSPORT_NUMBER_SHORT;
import static ru.clevertec.houses.util.ConstantsTest.INCORRECT_PASSPORT_SERIES;
import static ru.clevertec.houses.util.PersonDtoTestData.validPersonBuilder;

class PersonDtoValidatorTest {

    private final PersonDtoValidator validator = new PersonDtoValidator();

    @Nested
    class IsValid {

        @Test
        void checkIsValidShouldReturnTrue() {
            //given
            PersonDto dto = validPersonBuilder().build();

            //when//then
            Assertions.assertTrue(validator.isValid(dto));
        }

        @ParameterizedTest
        @MethodSource("argsForValidationException")
        void checkIsValidShouldThrowsValidationException(PersonDto argDto) {
            assertThrows(ValidationException.class,
                    () -> validator.isValid(argDto));
        }

        static Stream<PersonDto> argsForValidationException() {
            return Stream.of(
                    validPersonBuilder()
                            .surname(EMPTY_STRING).build(),
                    validPersonBuilder()
                            .surname(null).build(),
                    validPersonBuilder()
                            .name(EMPTY_STRING).build(),
                    validPersonBuilder()
                            .name(null).build(),
                    validPersonBuilder()
                            .passportSeries(null).build(),
                    validPersonBuilder()
                            .passportSeries(EMPTY_STRING).build(),
                    validPersonBuilder()
                            .passportSeries(INCORRECT_PASSPORT_SERIES).build(),
                    validPersonBuilder()
                            .passportNumber(INCORRECT_PASSPORT_NUMBER_LONG).build(),
                    validPersonBuilder()
                            .passportNumber(INCORRECT_PASSPORT_NUMBER_SHORT).build(),
                    validPersonBuilder()
                            .passportNumber(EMPTY_STRING).build(),
                    validPersonBuilder()
                            .passportNumber(null).build(),
                    null
            );
        }
    }

    @Nested
    class IsValidWithNotNullUuid {

        @Test
        void checkIsValidShouldThrowsValidationException() {
            //given
            PersonDto noValidDto = validPersonBuilder().uuid(null).build();

            //when//then
            assertThrows(ValidationException.class,
                    () -> validator.isValidWithNotNullUuid(noValidDto));
        }
    }

}
