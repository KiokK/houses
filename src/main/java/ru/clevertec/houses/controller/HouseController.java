package ru.clevertec.houses.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.houses.dto.PaginationInfoDto;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.HouseResidentsDto;
import ru.clevertec.houses.dto.error.ErrorResponseDto;
import ru.clevertec.houses.dto.response.HouseHistoryDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.model.enums.HistoryType;
import ru.clevertec.houses.service.HouseService;

import jakarta.validation.Valid;
import java.util.UUID;

import static ru.clevertec.houses.dto.error.ErrorCodeConstants.ENTITY_NOT_MODIFIED;
import static ru.clevertec.houses.dto.error.ErrorMessagesConstants.M_NOT_DELETED;

@Tag(name = "House")
@RestController
@RequiredArgsConstructor
@RequestMapping("/houses")
public class HouseController {

    private final HouseService houseService;

    @Operation(description = "Get all persons with pagination")
    @GetMapping
    public ResponseEntity<PaginationResponseDto> findAll(@Valid @ModelAttribute PaginationInfoDto paginationInfo) {
        Pageable pageable = PageRequest.of(paginationInfo.getPageNumber(), paginationInfo.getPageSize());

        return ResponseEntity.ok(houseService.findAll(pageable));
    }

    @Operation(description = "Get history about all persons who live in house with pagination")
    @GetMapping(value = "/{uuid}/history/tenants")
    public ResponseEntity<HouseHistoryDto> findAllWhichEverLiveInHouse(@PathVariable UUID uuid,
                                                                       @Valid @ModelAttribute PaginationInfoDto paginationInfo) {
        Pageable pageable = PageRequest.of(paginationInfo.getPageNumber(), paginationInfo.getPageSize());

        return ResponseEntity.ok(houseService.findPersonsByHouseUuidAndHistoryType(uuid, pageable, HistoryType.TENANT));
    }

    @Operation(description = "Get history about all persons-owners in house with pagination")
    @GetMapping(value = "/{uuid}/history/owners")
    public ResponseEntity<HouseHistoryDto> findAllHistoryOwnersInHouse(@PathVariable UUID uuid,
                                                                       @Valid @ModelAttribute PaginationInfoDto paginationInfo) {
        Pageable pageable = PageRequest.of(paginationInfo.getPageNumber(), paginationInfo.getPageSize());

        return ResponseEntity.ok(houseService.findPersonsByHouseUuidAndHistoryType(uuid, pageable, HistoryType.OWNER));
    }

    @Operation(description = "Get house by uuid")
    @GetMapping(value = "/{uuid}")
    public ResponseEntity<?> findHouseByUuid(@PathVariable UUID uuid) {
        HouseDto dto = houseService.findHouseByUuid(uuid);

        return ResponseEntity.ok(dto);
    }

    @Operation(description = "Get house with resients by uuid")
    @GetMapping(value = "/{uuid}/with_residents")
    public ResponseEntity<?> findHouseResidentsByHouseUuid(@PathVariable UUID uuid) {
        HouseResidentsDto dto = houseService.findAllResidentsByHouseUuid(uuid);

        return ResponseEntity.ok(dto);
    }

    @Operation(description = "Create new house")
    @PostMapping(value = "/create")
    public ResponseEntity<?> createHouseDto(@Valid @RequestBody HouseDto houseDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(houseService.create(houseDto));
    }

    @Operation(description = "Update house by uuid")
    @PutMapping(value = "/{uuid}/update")
    public ResponseEntity<?> updateHouseDto(@PathVariable UUID uuid, @Valid @RequestBody HouseDto houseDto) {
        HouseDto dto = houseService.update(uuid, houseDto);

        return ResponseEntity.ok(dto);
    }

    @Operation(description = "Delete house by uuid")
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteHouseByUuid(@RequestParam UUID uuid) {
        boolean isDeleted = houseService.deleteByUuid(uuid);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(String.format(M_NOT_DELETED, "uuid", uuid), ENTITY_NOT_MODIFIED));
    }

}
