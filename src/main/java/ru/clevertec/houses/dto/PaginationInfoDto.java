package ru.clevertec.houses.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaginationInfoDto {

    @Min(0)
    private Integer pageNumber;

    @Min(1)
    private Integer pageSize;

    public static final PaginationInfoDto DEFAULT = new PaginationInfoDto(0, 15);

    public PaginationInfoDto() {
        this.pageNumber = DEFAULT.pageNumber;
        this.pageSize = DEFAULT.pageSize;
    }

}
