package ru.clevertec.houses.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.clevertec.houses.model.Person;
import ru.clevertec.houses.model.enums.HistoryType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonDao extends JpaRepository<Person, Long> {

    Optional<Person> findByUuid(UUID uuid);

    Page<Person> findAll(Pageable pageable);

    @Query("SELECT p FROM Person p JOIN FETCH p.houses WHERE p.uuid = :uuid")
    Optional<Person> findWithHousesByPersonUuid(@Param("uuid") UUID uuid);

    @Query(value = "SELECT p.* FROM house h JOIN house_history hh ON hh.house_id = h.id JOIN person p ON p.id = hh.person_id " +
            "WHERE h.uuid = :houseUuid AND hh.type = CAST(:#{#historyType?.name()} as relation_house_type)", nativeQuery = true)
    List<Person> findAllPersonsByHouseUuidAndHistoryType(@Param("houseUuid") UUID houseUuid, Pageable pageable, @Param("historyType") HistoryType historyType);

    @Modifying
    @Query("delete from Person p where p.uuid=:uuid")
    int deletePersonByUuid(@Param("uuid") UUID uuid);

}
