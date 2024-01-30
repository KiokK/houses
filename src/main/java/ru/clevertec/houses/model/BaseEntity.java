package ru.clevertec.houses.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import static ru.clevertec.houses.model.Patterns.DATA_FORMAT;

@Data
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    @EqualsAndHashCode.Exclude
    @JsonFormat(pattern = DATA_FORMAT)
    @Column(name = "create_date", updatable = false, nullable = false)
    private LocalDateTime createDate;

}
