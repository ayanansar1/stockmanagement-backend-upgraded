package com.stockmanagement.stockmanagement.product.entity;


import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "products",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_code",
                        columnNames = "product_code"
                ),
                @UniqueConstraint(
                        name = "uk_product_name",
                        columnNames = "product_name"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(
            name = "product_code",
            nullable = false,
            length = 30
    )
    private String productCode;

    @Column(
            name = "product_name",
            nullable = false,
            length = 100
    )
    private String productName;

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