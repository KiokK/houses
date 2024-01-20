package ru.clevertec.houses.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ru.clevertec.houses.dao.model.PaginationInfo;
import ru.clevertec.houses.dto.HouseDto;
import ru.clevertec.houses.dto.HouseResidentsDto;
import ru.clevertec.houses.dto.error.ErrorResponseDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.service.HouseService;

import java.util.UUID;

import static ru.clevertec.houses.dto.error.ErrorCodeConstants.ENTITY_NOT_MODIFIED;
import static ru.clevertec.houses.dto.error.ErrorMessagesConstants.M_NOT_DELETED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/houses")
public class HouseController {

    private final HouseService houseService;

    @GetMapping
    public ResponseEntity<PaginationResponseDto> findAll(@ModelAttribute(value = "paginationInfo") PaginationInfo paginationInfo) {
        return ResponseEntity.ok(houseService.findAll(paginationInfo));
    }

    @GetMapping(value = "/{uuid}")
    public ResponseEntity<?> findHouseByUuid(@PathVariable("uuid") UUID uuid) {
        HouseDto dto = houseService.findHouseByUuid(uuid);

        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{uuid}/with_residents")
    public ResponseEntity<?> findHouseResidentsByHouseUuid(@PathVariable("uuid") UUID uuid) {
        HouseResidentsDto dto = houseService.findAllResidentsByHouseUuid(uuid);

        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> createHouseDto(@Valid @RequestBody HouseDto houseDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(houseService.create(houseDto));
    }

    @PutMapping(value = "/{uuid}/update")
    public ResponseEntity<?> updateHouseDto(@PathVariable("uuid") UUID uuid, @Valid @RequestBody HouseDto houseDto) {
        HouseDto dto = houseService.update(uuid, houseDto);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteHouseByUuid(@RequestParam("uuid") UUID uuid) {
        boolean isDeleted = houseService.deleteByUuid(uuid);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(String.format(M_NOT_DELETED, "uuid", uuid), ENTITY_NOT_MODIFIED));
    }

}
