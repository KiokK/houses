package ru.clevertec.houses.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.clevertec.houses.model.enums.Gender;

import java.util.UUID;

import static ru.clevertec.houses.model.Patterns.NAME_PATTERN;
import static ru.clevertec.houses.model.Patterns.PASSPORT_NUMBER_PATTERN;
import static ru.clevertec.houses.model.Patterns.PASSPORT_SERIES_PATTERN;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

    public UUID uuid;

    @NotBlank
    @Size(max = 32)
    @Pattern(regexp = NAME_PATTERN)
    public String name;

    @NotBlank
    @Size(max = 32)
    @Pattern(regexp = NAME_PATTERN)
    public String surname;

    @NotNull
    public Gender sex;

    @NotBlank
    @Pattern(regexp = PASSPORT_SERIES_PATTERN)
    public String passportSeries;

    @NotBlank
    @Pattern(regexp = PASSPORT_NUMBER_PATTERN)
    public String passportNumber;

    @NotNull
    public UUID houseLiveUuid;

}
