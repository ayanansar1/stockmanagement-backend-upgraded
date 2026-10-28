package com.stockmanagement.stockmanagement.stockmovement.entity;


import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.StockUnit;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.entity.BanaKareegar;
import com.stockmanagement.stockmanagement.mapping.designKareegar.entity.DesignKareegar;
import com.stockmanagement.stockmanagement.mapping.productKareegar.entity.ProductKareegar;
import com.stockmanagement.stockmanagement.productadd.entity.ProductAdd;
import com.stockmanagement.stockmanagement.productreturn.entity.ProductReturn;
import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import com.stockmanagement.stockmanagement.stockmovement.enums.StockMovementType;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "stock_movements",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_stock_movement_code",
                        columnNames = "movement_code"
                )
        },
        indexes = {
                @Index(
                        name = "idx_stock_movement_kareegar",
                        columnList = "kareegar_id"
                ),
                @Index(
                        name = "idx_stock_movement_machine",
                        columnList = "machine_id"
                ),
                @Index(
                        name = "idx_stock_movement_product",
                        columnList = "product_kareegar_id"
                ),
                @Index(
                        name = "idx_stock_movement_date",
                        columnList = "movement_date"
                ),
                @Index(
                        name = "idx_stock_movement_type",
                        columnList = "movement_type"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockMovement extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(
            name = "movement_code",
            nullable = false,
            length = 30
    )
    private String movementCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_add_id",
            foreignKey = @ForeignKey(
                    name = "fk_stock_movement_product_add"
            )
    )
    private ProductAdd productAdd;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_sell_id",
            foreignKey = @ForeignKey(
                    name = "fk_stock_movement_product_sell"
            )
    )
    private ProductSell productSell;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_return_id",
            foreignKey = @ForeignKey(name = "fk_stock_movement_product_return")
    )
    private ProductReturn productReturn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_stock_movement_kareegar"
            )
    )
    private Kareegar kareegar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "machine_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_stock_movement_machine"
            )
    )
    private Machine machine;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_stock_movement_product_kareegar"
            )
    )
    private ProductKareegar productKareegar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "bana_kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_stock_movement_bana_kareegar"
            )
    )
    private BanaKareegar banaKareegar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "design_kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_stock_movement_design_kareegar"
            )
    )
    private DesignKareegar designKareegar;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "movement_type",
            nullable = false,
            length = 20
    )
    private StockMovementType movementType;

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
            name = "movement_date",
            nullable = false
    )
    private LocalDate movementDate;
}