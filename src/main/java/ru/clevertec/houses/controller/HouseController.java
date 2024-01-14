package ru.clevertec.houses.controller;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
import ru.clevertec.houses.service.HouseService;
import ru.clevertec.houses.validator.dto.HouseDtoValidator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static ru.clevertec.houses.dto.error.ErrorCodeConstants.HOUSE_NOT_FOUND;
import static ru.clevertec.houses.dto.error.ErrorCodeConstants.HOUSE_NOT_MODIFIED;
import static ru.clevertec.houses.dto.error.ErrorCodeConstants.REQUEST_NOT_VALID;
import static ru.clevertec.houses.dto.error.ErrorMessagesConstants.M_NOT_DELETED;
import static ru.clevertec.houses.dto.error.ErrorMessagesConstants.M_NOT_FOUND;
import static ru.clevertec.houses.dto.error.ErrorMessagesConstants.M_NOT_VALID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/house")
public class HouseController {

    private final HouseService houseService;

    private final HouseDtoValidator validator;

    @GetMapping(value = "/find_all")
    public ResponseEntity<List<HouseDto>> findAll(@RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
                                                  @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber) {
        PaginationInfo paginationInfo = new PaginationInfo(pageNumber, pageSize);
        return ResponseEntity.ok(houseService.findAll(paginationInfo));
    }

    @GetMapping(value = "/find_by_uuid")
    public ResponseEntity<?> findHouseByUuid(@RequestParam("uuid") UUID uuid) {
        Optional<HouseDto> dto = houseService.findHouseByUuid(uuid);
        if (dto.isPresent()) {
            return ResponseEntity.ok(dto.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(String.format(M_NOT_FOUND, "uuid", uuid), HOUSE_NOT_FOUND));
    }

    @GetMapping(value = "/find/with_residents")
    public ResponseEntity<?> findHouseResidentsByHouseUuid(@RequestParam("uuid") UUID uuid) {
        Optional<HouseResidentsDto> dto = houseService.findAllResidentsByHouseUuid(uuid);
        if (dto.isPresent()) {
            return ResponseEntity.ok(dto.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(String.format(M_NOT_FOUND, "uuid", uuid), HOUSE_NOT_FOUND));
    }

    @PutMapping(value = "/create")
    public ResponseEntity<?> createHouseDto(@RequestBody HouseDto houseDto) {
        try {
            validator.isValid(houseDto);
        } catch (ValidationException validationException) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto(String.format(M_NOT_VALID, "requestDto", houseDto), REQUEST_NOT_VALID));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(houseService.create(houseDto));
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> updateHouseDto(@RequestBody HouseDto houseDto) {
        try {
            validator.isValidWithNotNullUuid(houseDto);
        } catch (ValidationException validationException) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto(String.format(M_NOT_VALID, "requestDto", houseDto), REQUEST_NOT_VALID));
        }

        Optional<HouseDto> dto = houseService.update(houseDto);
        if (dto.isPresent()) {
            return ResponseEntity.ok(dto.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(String.format(M_NOT_FOUND, "uuid", houseDto.uuid), HOUSE_NOT_FOUND));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteHouseByUuid(@RequestParam("uuid") UUID uuid) {
        boolean isDeleted = houseService.deleteByUuid(uuid);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(String.format(M_NOT_DELETED, "uuid", uuid), HOUSE_NOT_MODIFIED));
    }

}
