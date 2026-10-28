package com.stockmanagement.stockmanagement.design.entity;


import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "designs",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_design_code",
                        columnNames = "design_code"
                ),
                @UniqueConstraint(
                        name = "uk_design_name",
                        columnNames = "design_name"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Design extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(
            name = "design_code",
            nullable = false,
            length = 30
    )
    private String designCode;

    @Column(
            name = "design_name",
            nullable = false,
            length = 100
    )
    private String designName;

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