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

    @Query(value = "SELECT h.* FROM house h JOIN house_history hh ON hh.house_id = h.id JOIN person p ON p.id = hh.person_id " +
            "WHERE p.uuid = :personUuid AND hh.type = CAST(:#{#historyType?.name()} as relation_house_type)", nativeQuery = true)
    List<House> findAllHousesByPersonUuidAndHistoryType(@Param("personUuid") UUID personUuid, Pageable pageable, @Param("historyType") HistoryType historyType);

    int deleteByUuid(UUID uuid);

}
