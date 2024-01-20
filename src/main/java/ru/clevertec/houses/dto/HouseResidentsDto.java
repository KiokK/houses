package ru.clevertec.houses.dto;

import jakarta.validation.constraints.NotNull;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

/**
 * Список всех Person проживающих в House
*/
@ToString
public class HouseResidentsDto {

    @NotNull
    public UUID uuid;

    public List<PersonDto> residents;

}
