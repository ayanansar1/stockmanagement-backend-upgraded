package com.stockmanagement.stockmanagement.bana.entity;

import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "banas",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_bana_code",
                        columnNames = "bana_code"
                ),
                @UniqueConstraint(
                        name = "uk_bana_name",
                        columnNames = "bana_name"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Bana extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(
            name = "bana_code",
            nullable = false,
            length = 30
    )
    private String banaCode;

    @Column(
            name = "bana_name",
            nullable = false,
            length = 100
    )
    private String banaName;

    @Column(
            name = "date_of_join",
            nullable = false
    )
    private LocalDate dateOfJoin;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private Status status;
}