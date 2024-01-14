package ru.clevertec.houses.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.ToString;
import ru.clevertec.houses.model.House;

import java.util.List;
import java.util.UUID;

@ToString
public class PersonsHouseRequestDto {

    @NotNull
    public UUID personUuid;

    public List<House> houses;

}
