package com.stockmanagement.stockmanagement.stockbalance.entity;


import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.StockUnit;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.entity.BanaKareegar;
import com.stockmanagement.stockmanagement.mapping.designKareegar.entity.DesignKareegar;
import com.stockmanagement.stockmanagement.mapping.productKareegar.entity.ProductKareegar;


import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "product_stock_balances",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_stock_balance",
                        columnNames = {
                                "kareegar_id",
                                "machine_id",
                                "product_kareegar_id",
                                "bana_kareegar_id",
                                "design_kareegar_id",
                                "unit"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_stock_balance_kareegar",
                        columnList = "kareegar_id"
                ),
                @Index(
                        name = "idx_stock_balance_machine",
                        columnList = "machine_id"
                ),
                @Index(
                        name = "idx_stock_balance_product",
                        columnList = "product_kareegar_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductStockBalance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;



    @Column(
            name = "stock_balance_code",
            nullable = false,
            unique = true,
            length = 30
    )
    private String stockBalanceCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_stock_balance_kareegar"
            )
    )
    private Kareegar kareegar;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "machine_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_stock_balance_machine"
            )
    )
    private Machine machine;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_stock_balance_product_kareegar"
            )
    )
    private ProductKareegar productKareegar;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "bana_kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_stock_balance_bana_kareegar"
            )
    )
    private BanaKareegar banaKareegar;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "design_kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_stock_balance_design_kareegar"
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
}