package com.stockmanagement.stockmanagement.productlisting.entity;

import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.stockbalance.entity.ProductStockBalance;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "product_listing_stock_sources",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_listing_stock_source",
                        columnNames = {"listing_id", "stock_balance_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_listing_source_listing",
                        columnList = "listing_id"
                ),
                @Index(
                        name = "idx_listing_source_balance",
                        columnList = "stock_balance_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ProductListingStockSource extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "listing_id", nullable = false)
    private ProductListing listing;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stock_balance_id", nullable = false)
    private ProductStockBalance stockBalance;

    @Column(nullable = false)
    private boolean enabled = true;
}