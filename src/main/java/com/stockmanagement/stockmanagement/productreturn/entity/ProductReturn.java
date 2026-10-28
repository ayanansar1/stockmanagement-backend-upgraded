package com.stockmanagement.stockmanagement.productreturn.entity;

import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.enums.StockUnit;
import com.stockmanagement.stockmanagement.productreturn.enums.ReturnReason;
import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "product_returns",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_return_code",
                        columnNames = "product_return_code"
                )
        },
        indexes = {
                @Index(
                        name = "idx_product_return_sell",
                        columnList = "product_sell_id"
                ),
                @Index(
                        name = "idx_product_return_date",
                        columnList = "return_date"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductReturn extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(
            name = "product_return_code",
            nullable = false,
            length = 30
    )
    private String productReturnCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_sell_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_product_return_product_sell"
            )
    )
    private ProductSell productSell;

    @Column(
            name = "return_quantity",
            nullable = false,
            precision = 15,
            scale = 3
    )
    private BigDecimal returnQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StockUnit unit;

    @Column(name = "return_date", nullable = false)
    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "return_reason",
            nullable = false,
            length = 40
    )
    private ReturnReason returnReason;

    @Column(length = 500)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Status status = Status.ACTIVE;
}