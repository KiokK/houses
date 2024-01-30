package ru.clevertec.houses.dto;

import jakarta.validation.constraints.NotNull;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

/**
 * Список всех House, владельцем которых является Person
 */
@ToString
public class PersonsHouseDto {

    @NotNull
    public UUID uuid;

    public List<HouseDto> ownHouses;

}
