package ru.clevertec.houses.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@ToString
public class PersonsHouseRequestDto {

    public UUID personUuid;

    @NotNull
    public List<UUID> houseUuids;

}
