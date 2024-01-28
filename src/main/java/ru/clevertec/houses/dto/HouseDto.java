package ru.clevertec.houses.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class HouseDto {

    public UUID uuid;

    @Min(1)
    @NotNull
    public Float area;

    @NotBlank
    public String country;

    @NotBlank
    public String city;

    @NotBlank
    public String street;

    @NotNull
    public Integer number;

}
