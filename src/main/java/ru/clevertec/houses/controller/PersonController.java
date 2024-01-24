package ru.clevertec.houses.controller;

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
import ru.clevertec.houses.dao.model.PaginationInfo;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public ResponseEntity<PaginationResponseDto> findAll(@ModelAttribute(value = "paginationInfo") PaginationInfo paginationInfo) {
        Pageable pageable = PageRequest.of(paginationInfo.getPageNumber(), paginationInfo.getPageSize());

        return ResponseEntity.ok(personService.findAll(pageable));
    }

    @GetMapping(value = "/{uuid}/history/housing")
    public ResponseEntity<PersonHistoryDto> findAllHousesInWhichEverLive(@PathVariable("uuid") UUID uuid,
                                                                         @ModelAttribute(value = "paginationInfo") PaginationInfo paginationInfo) {
        Pageable pageable = PageRequest.of(paginationInfo.getPageNumber(), paginationInfo.getPageSize());

        return ResponseEntity.ok(personService.findHousesByPersonUuidAndHistoryType(uuid, pageable, HistoryType.TENANT));
    }

    @GetMapping(value = "/{uuid}/history/ownerships")
    public ResponseEntity<PersonHistoryDto> historyAboutOwnerships(@PathVariable("uuid") UUID uuid,
                                                                   @ModelAttribute(value = "paginationInfo") PaginationInfo paginationInfo) {
        Pageable pageable = PageRequest.of(paginationInfo.getPageNumber(), paginationInfo.getPageSize());

        return ResponseEntity.ok(personService.findHousesByPersonUuidAndHistoryType(uuid, pageable, HistoryType.OWNER));
    }

    @GetMapping(value = "/{uuid}")
    public ResponseEntity<?> findPersonByUuid(@PathVariable("uuid") UUID uuid) {
        PersonDto dto = personService.findPersonByUuid(uuid);

        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/{uuid}/with_houses")
    public ResponseEntity<?> findAllOwnHousesByPersonUuid(@PathVariable("uuid") UUID uuid) {
        PersonsHouseDto dto = personService.findAllOwnHousesByPersonUuid(uuid);

        return ResponseEntity.ok(dto);
    }


    @PostMapping(value = "/create")
    public ResponseEntity<?> createPerson(@Valid @RequestBody PersonDto personDto) {
        PersonDto createdPersonDto = personService.create(personDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdPersonDto);
    }

    @PutMapping(value = "/{uuid}/update/houses")
    public ResponseEntity<?> updatePersonWithHousesDto(@PathVariable("uuid") UUID uuid,
                                                       @Valid @RequestBody PersonsHouseRequestDto personsHouseDto) {
        boolean isUpdated = personService.update(uuid, personsHouseDto);
        if (isUpdated) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(String.format(M_NOT_UPDATED, "requestDto", personsHouseDto), ENTITY_NOT_MODIFIED));
    }

    @PutMapping(value = "/{uuid}/update")
    public ResponseEntity<?> updatePersonInfoDto(@PathVariable("uuid") UUID uuid, @Valid @RequestBody PersonDto personDto) {
        PersonDto updatedPerson = personService.update(uuid, personDto);

        return ResponseEntity.ok(updatedPerson);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deletePersonByUuid(@RequestParam("uuid") UUID uuid) {
        boolean isDeleted = personService.deleteByUuid(uuid);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(String.format(M_NOT_DELETED, "uuid", uuid), ENTITY_NOT_MODIFIED));
    }

}
