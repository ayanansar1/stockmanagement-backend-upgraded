package com.stockmanagement.stockmanagement.productadd.entity;


import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.enums.StockUnit;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.entity.BanaKareegar;
import com.stockmanagement.stockmanagement.mapping.designKareegar.entity.DesignKareegar;
import com.stockmanagement.stockmanagement.mapping.productKareegar.entity.ProductKareegar;



import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "product_adds",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_add_code",
                        columnNames = "product_add_code"
                )
        },
        indexes = {
                @Index(
                        name = "idx_product_add_kareegar",
                        columnList = "kareegar_id"
                ),
                @Index(
                        name = "idx_product_add_machine",
                        columnList = "machine_id"
                ),
                @Index(
                        name = "idx_product_add_product_kareegar",
                        columnList = "product_kareegar_id"
                ),
                @Index(
                        name = "idx_product_add_bana_kareegar",
                        columnList = "bana_kareegar_id"
                ),
                @Index(
                        name = "idx_product_add_design_kareegar",
                        columnList = "design_kareegar_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAdd extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(
            name = "product_add_code",
            nullable = false,
            length = 30
    )
    private String productAddCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_product_add_kareegar"
            )
    )
    private Kareegar kareegar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "machine_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_product_add_machine"
            )
    )
    private Machine machine;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_product_add_product_kareegar"
            )
    )
    private ProductKareegar productKareegar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "bana_kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_product_add_bana_kareegar"
            )
    )
    private BanaKareegar banaKareegar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "design_kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_product_add_design_kareegar"
            )
    )
    private DesignKareegar designKareegar;

    @Column(
            nullable = false,
            precision = 15,
            scale = 3
    )
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private StockUnit unit;

    @Column(
            name = "added_date",
            nullable = false
    )
    private LocalDate addedDate;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private Status status;
}