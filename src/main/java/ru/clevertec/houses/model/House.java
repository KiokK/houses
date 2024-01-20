package ru.clevertec.houses.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.UUIDJdbcType;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "house")
public class House extends BaseEntity {

    @JdbcType(UUIDJdbcType.class)
    @Column(columnDefinition = "uuid", unique = true, nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private Float area;

    @Column(length = 30, nullable = false)
    private String country;

    @Column(length = 30, nullable = false)
    private String city;

    @Column(length = 30, nullable = false)
    private String street;

    @Column(columnDefinition = "int", nullable = false)
    private Integer number;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "residentOf", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Person> residents;

}
