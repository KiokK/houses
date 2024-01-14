package ru.clevertec.houses.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.clevertec.houses.model.Patterns.NAME_PATTERN;
import static ru.clevertec.houses.model.Patterns.PASSPORT_NUMBER_PATTERN;
import static ru.clevertec.houses.model.Patterns.PASSPORT_SERIES_PATTERN;

@Entity
@Data
@Table(name = "person")
@EqualsAndHashCode(callSuper = true)
public class Person extends BaseEntity  {

    @JdbcType(UUIDJdbcType.class)
    @Column(columnDefinition = "uuid", unique = true, nullable = false)
    private UUID uuid;

    @Pattern(regexp = NAME_PATTERN)
    @Column(length = 32, nullable = false)
    private String name;

    @Pattern(regexp = NAME_PATTERN)
    @Column(length = 32, nullable = false)
    private String surname;

    @Enumerated(EnumType.STRING)
    private Gender sex;

    @Pattern(regexp = PASSPORT_SERIES_PATTERN)
    @Column(name = "passport_series", length = 2, nullable = false)
    private String passportSeries;

    @Pattern(regexp = PASSPORT_NUMBER_PATTERN)
    @Column(name = "passport_number", length = 7, nullable = false)
    private String passportNumber;

    @Column(name = "update_date", nullable = false)
    @EqualsAndHashCode.Exclude
    private LocalDateTime updateDate;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "house_live_id", referencedColumnName = "id")
    private House residentOf;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "person_owner_house",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "house_id")
    )
    @EqualsAndHashCode.Exclude
    private List<House> houses;

}
