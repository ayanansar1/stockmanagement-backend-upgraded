package com.stockmanagement.stockmanagement.productlisting.entity;

import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import com.stockmanagement.stockmanagement.product.entity.Product;
import com.stockmanagement.stockmanagement.productlisting.enums.ListingStatus;
import com.stockmanagement.stockmanagement.productlisting.enums.ListingType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "product_listings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_product_listing_code",
                        columnNames = "listing_code"
                ),
                @UniqueConstraint(
                        name = "uk_product_listing_slug",
                        columnNames = "slug"
                )
        },
        indexes = {
                @Index(
                        name = "idx_listing_status_deleted",
                        columnList = "listing_status, deleted"
                ),
                @Index(
                        name = "idx_listing_type_status",
                        columnList = "listing_type, listing_status"
                ),
                @Index(
                        name = "idx_listing_product",
                        columnList = "product_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ProductListing extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "listing_code", nullable = false, length = 30)
    private String listingCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "listing_type", nullable = false, length = 30)
    private ListingType listingType = ListingType.ONLINE_STORE;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 220)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(
            name = "selling_price",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal sellingPrice;

    /*
     * Optional cap on quantity offered online.
     * This is not a separate physical-stock balance.
     */
    @Column(name = "online_stock_limit", precision = 15, scale = 3)
    private BigDecimal onlineStockLimit;

    @Enumerated(EnumType.STRING)
    @Column(name = "listing_status", nullable = false, length = 20)
    private ListingStatus listingStatus = ListingStatus.DRAFT;

    @OneToMany(
            mappedBy = "listing",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("displayOrder ASC")
    private List<ProductListingImage> images = new ArrayList<>();

    @OneToMany(
            mappedBy = "listing",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductListingStockSource> stockSources = new ArrayList<>();

    public void addImage(ProductListingImage image) {
        image.setListing(this);
        images.add(image);
    }

    public void addStockSource(ProductListingStockSource source) {
        source.setListing(this);
        stockSources.add(source);
    }

    public void clearStockSources() {
        stockSources.clear();
    }
}