package ru.clevertec.houses.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
import ru.clevertec.houses.dto.PersonDto;
import ru.clevertec.houses.dto.PersonsHouseDto;
import ru.clevertec.houses.dto.error.ErrorResponseDto;
import ru.clevertec.houses.dto.request.PersonsHouseRequestDto;
import ru.clevertec.houses.dto.response.PaginationResponseDto;
import ru.clevertec.houses.dto.response.PersonHistoryDto;
import ru.clevertec.houses.model.enums.HistoryType;
import ru.clevertec.houses.service.PersonService;

import java.util.UUID;

import static ru.clevertec.houses.dto.error.ErrorCodeConstants.ENTITY_NOT_MODIFIED;
import static ru.clevertec.houses.dto.error.ErrorMessagesConstants.M_NOT_DELETED;
import static ru.clevertec.houses.dto.error.ErrorMessagesConstants.M_NOT_UPDATED;

@Tag(name = "Person")
@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @Operation(description = "Get all persons with pagination")
    @GetMapping
    public ResponseEntity<PaginationResponseDto> findAll(@Valid @ModelAttribute PaginationInfoDto paginationInfo) {
        Pageable pageable = PageRequest.of(paginationInfo.getPageNumber(), paginationInfo.getPageSize());

        return ResponseEntity.ok(personService.findAll(pageable));
    }

    @Operation(description = "Get all houses in which person live with pagination")
    @GetMapping(value = "/{uuid}/history/housing")
    public ResponseEntity<PersonHistoryDto> findAllHousesInWhichEverLive(@PathVariable UUID uuid,
                                                                         @Valid @ModelAttribute PaginationInfoDto paginationInfo) {
        Pageable pageable = PageRequest.of(paginationInfo.getPageNumber(), paginationInfo.getPageSize());

        return ResponseEntity.ok(personService.findHousesByPersonUuidAndHistoryType(uuid, pageable, HistoryType.TENANT));
    }

    @Operation(description = "Get history about person's ownerships")
    @GetMapping(value = "/{uuid}/history/ownerships")
    public ResponseEntity<PersonHistoryDto> historyAboutOwnerships(@PathVariable("uuid") UUID uuid,
                                                                   @Valid @ModelAttribute PaginationInfoDto paginationInfo) {
        Pageable pageable = PageRequest.of(paginationInfo.getPageNumber(), paginationInfo.getPageSize());

        return ResponseEntity.ok(personService.findHousesByPersonUuidAndHistoryType(uuid, pageable, HistoryType.OWNER));
    }

    @Operation(description = "Get person by uuid")
    @GetMapping(value = "/{uuid}")
    public ResponseEntity<?> findPersonByUuid(@PathVariable UUID uuid) {
        PersonDto dto = personService.findPersonByUuid(uuid);

        return ResponseEntity.ok(dto);
    }

    @Operation(description = "Get person with houses")
    @GetMapping(value = "/{uuid}/with_houses")
    public ResponseEntity<?> findAllOwnHousesByPersonUuid(@PathVariable UUID uuid) {
        PersonsHouseDto dto = personService.findAllOwnHousesByPersonUuid(uuid);

        return ResponseEntity.ok(dto);
    }

    @Operation(description = "Create person")
    @PostMapping(value = "/create")
    public ResponseEntity<?> createPerson(@Valid @RequestBody PersonDto personDto) {
        PersonDto createdPersonDto = personService.create(personDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdPersonDto);
    }

    @Operation(description = "Update person houses")
    @PutMapping(value = "/{uuid}/update/houses")
    public ResponseEntity<?> updatePersonWithHousesDto(@PathVariable UUID uuid,
                                                       @Valid @RequestBody PersonsHouseRequestDto personsHouseDto) {
        boolean isUpdated = personService.update(uuid, personsHouseDto);
        if (isUpdated) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(String.format(M_NOT_UPDATED, "requestDto", personsHouseDto), ENTITY_NOT_MODIFIED));
    }

    @Operation(description = "Update person info")
    @PutMapping(value = "/{uuid}/update")
    public ResponseEntity<?> updatePersonInfoDto(@PathVariable UUID uuid, @Valid @RequestBody PersonDto personDto) {
        PersonDto updatedPerson = personService.update(uuid, personDto);

        return ResponseEntity.ok(updatedPerson);
    }

    @Operation(description = "Delete person by uuid")
    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deletePersonByUuid(@RequestParam UUID uuid) {
        boolean isDeleted = personService.deleteByUuid(uuid);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(String.format(M_NOT_DELETED, "uuid", uuid), ENTITY_NOT_MODIFIED));
    }

}
