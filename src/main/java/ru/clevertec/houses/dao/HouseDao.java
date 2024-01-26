package ru.clevertec.houses.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.houses.model.House;
import ru.clevertec.houses.model.enums.HistoryType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HouseDao extends JpaRepository<House, Long> {

    Optional<House> findByUuid(UUID uuid);

    Page<House> findAll(Pageable pageable);

    List<House> findAllByUuidIn(List<UUID> uuidList);

    @Query("SELECT h FROM House h JOIN FETCH h.residents WHERE h.uuid = :uuid")
    Optional<House> findWithResidentsByUuid(@Param("uuid") UUID uuid);

    List<House> findAllByHouseHistory_person_uuidAndHouseHistory_type(UUID houseHistory_person_uuid, HistoryType houseHistory_type, Pageable pageable);

    int deleteByUuid(UUID uuid);

}
