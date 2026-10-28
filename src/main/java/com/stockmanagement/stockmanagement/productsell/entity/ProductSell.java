package com.stockmanagement.stockmanagement.productsell.entity;

import com.stockmanagement.stockmanagement.buyer.entity.Buyer;
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
        name = "product_sells",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_sell_code",
                        columnNames = "product_sell_code"
                )
        },
        indexes = {
                @Index(name = "idx_product_sell_kareegar", columnList = "kareegar_id"),
                @Index(name = "idx_product_sell_machine", columnList = "machine_id"),
                @Index(name = "idx_product_sell_product", columnList = "product_kareegar_id"),
                @Index(name = "idx_product_sell_buyer", columnList = "buyer_id"),
                @Index(name = "idx_product_sell_date", columnList = "sell_date")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSell extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "product_sell_code", nullable = false, length = 30)
    private String productSellCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_sell_kareegar")
    )
    private Kareegar kareegar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "machine_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_sell_machine")
    )
    private Machine machine;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_sell_product_kareegar")
    )
    private ProductKareegar productKareegar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "bana_kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_sell_bana_kareegar")
    )
    private BanaKareegar banaKareegar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "design_kareegar_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_sell_design_kareegar")
    )
    private DesignKareegar designKareegar;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "buyer_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_product_sell_buyer")
    )
    private Buyer buyer;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StockUnit unit;

    @Column(name = "sell_date", nullable = false)
    private LocalDate sellDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;
}