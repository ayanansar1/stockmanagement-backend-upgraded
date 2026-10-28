package com.stockmanagement.stockmanagement.salesinvoice.entity;

import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.common.enums.StockUnit;
import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "sales_invoice_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_invoice_product_sell",
                        columnNames = "product_sell_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesInvoiceItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "invoice_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_invoice_item_invoice")
    )
    private SalesInvoice invoice;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_sell_id",
            nullable = false,
            unique = true,
            foreignKey = @ForeignKey(name = "fk_invoice_item_product_sell")
    )
    private ProductSell productSell;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StockUnit unit;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal rate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;
}