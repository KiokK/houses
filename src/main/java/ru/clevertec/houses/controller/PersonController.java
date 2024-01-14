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
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.PersonsHouseDto;
import ru.clevertec.houses.dto.request.PersonsHouseRequestDto;
import ru.clevertec.houses.dto.error.ErrorResponseDto;
import ru.clevertec.houses.service.PersonService;
import ru.clevertec.houses.validator.dto.PersonDtoValidator;

import java.util.List;
import java.util.UUID;

import static ru.clevertec.houses.dto.error.ErrorCodeConstants.HOUSE_NOT_FOUND;
import static ru.clevertec.houses.dto.error.ErrorCodeConstants.PERSON_NOT_MODIFIED;
import static ru.clevertec.houses.dto.error.ErrorCodeConstants.PERSON_NOT_FOUND;
import static ru.clevertec.houses.dto.error.ErrorCodeConstants.REQUEST_NOT_VALID;
import static ru.clevertec.houses.dto.error.ErrorMessagesConstants.M_NOT_DELETED;
import static ru.clevertec.houses.dto.error.ErrorMessagesConstants.M_NOT_FOUND;
import static ru.clevertec.houses.dto.error.ErrorMessagesConstants.M_NOT_UPDATED;
import static ru.clevertec.houses.dto.error.ErrorMessagesConstants.M_NOT_VALID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    private final PersonDtoValidator validator;

    @GetMapping(value = "/find_all")
    public ResponseEntity<List<PersonDto>> findAll(@RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize,
                                                   @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber) {
        PaginationInfo paginationInfo = new PaginationInfo(pageNumber, pageSize);
        return ResponseEntity.ok(personService.findAll(paginationInfo));
    }

    @GetMapping(value = "/find_by_uuid")
    public ResponseEntity<?> findPersonByUuid(@RequestParam("uuid") UUID uuid) {
        PersonDto dto = personService.findPersonByUuid(uuid).orElse(null);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(String.format(M_NOT_FOUND, "uuid", uuid), PERSON_NOT_FOUND));
    }

    @GetMapping(value = "/find/with_houses")
    public ResponseEntity<?> findAllOwnHousesByPersonUuid(@RequestParam("uuid") UUID uuid) {
        PersonsHouseDto dto = personService.findAllOwnHousesByPersonUuid(uuid).orElse(null);
        if (dto != null) {
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(String.format(M_NOT_FOUND, "uuid", uuid), PERSON_NOT_FOUND));
    }


    @PutMapping(value = "/create")
    public ResponseEntity<?> createPersonDto(@RequestBody PersonDto personDto) {
        try {
            validator.isValid(personDto);
        } catch (ValidationException validationException) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto(String.format(M_NOT_VALID, "requestDto", personDto), REQUEST_NOT_VALID));
        }

        PersonDto createdPersonDto = personService.create(personDto);
        if (createdPersonDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto(String.format(M_NOT_FOUND, "uuid", personDto.houseLiveUuid), HOUSE_NOT_FOUND));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdPersonDto);
    }

    @PostMapping(value = "/update/houses")
    public ResponseEntity<?> updatePersonWithHousesDto(@RequestBody PersonsHouseRequestDto personsHouseDto) {
        boolean isUpdated = personService.update(personsHouseDto);
        if (isUpdated) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(String.format(M_NOT_UPDATED, "requestDto", personsHouseDto), PERSON_NOT_MODIFIED));
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> updatePersonInfoDto(@RequestBody PersonDto personDto) {
        try {
            validator.isValid(personDto);
        } catch (ValidationException validationException) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDto(String.format(M_NOT_VALID, "requestDto", personDto), REQUEST_NOT_VALID));
        }

        boolean isUpdated = personService.update(personDto);
        if (isUpdated) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(String.format(M_NOT_UPDATED, "requestDto", personDto), PERSON_NOT_MODIFIED));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deletePersonByUuid(@RequestParam("uuid") UUID uuid) {
        boolean isDeleted = personService.deleteByUuid(uuid);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(String.format(M_NOT_DELETED, "uuid", uuid), PERSON_NOT_MODIFIED));
    }

}
