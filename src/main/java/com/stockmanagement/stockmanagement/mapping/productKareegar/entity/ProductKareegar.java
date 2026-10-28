package com.stockmanagement.stockmanagement.mapping.productKareegar.entity;

import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "product_kareegars",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_kareegar_code",
                        columnNames = "product_kareegar_code"
                )
        },
        indexes = {
                @Index(
                        name = "idx_product_kareegar_kareegar",
                        columnList = "kareegar_id"
                ),
                @Index(
                        name = "idx_product_kareegar_product",
                        columnList = "product_id"
                ),
                @Index(
                        name = "idx_product_kareegar_machine",
                        columnList = "machine_id"
                ),
                @Index(
                        name = "idx_product_kareegar_status_deleted",
                        columnList = "status, deleted"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductKareegar extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(
            name = "product_kareegar_code",
            nullable = false,
            length = 30
    )
    private String productKareegarCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_product_kareegar_kareegar"
            )
    )
    private Kareegar kareegar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_product_kareegar_product"
            )
    )
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "machine_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_product_kareegar_machine"
            )
    )
    private Machine machine;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private Status status;

    @Column(
            name = "assigned_date",
            nullable = false
    )
    private LocalDate assignedDate;
}