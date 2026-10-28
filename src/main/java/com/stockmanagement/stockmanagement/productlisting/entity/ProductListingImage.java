package com.stockmanagement.stockmanagement.productlisting.entity;

import com.stockmanagement.stockmanagement.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "product_listing_images",
        indexes = {
                @Index(
                        name = "idx_listing_image_listing",
                        columnList = "listing_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ProductListingImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "listing_id", nullable = false)
    private ProductListing listing;

    @Column(name = "image_url", nullable = false, length = 1000)
    private String imageUrl;

    @Column(name = "storage_public_id", length = 255)
    private String storagePublicId;

    @Column(name = "alt_text", length = 255)
    private String altText;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder = 0;

    @Column(name = "primary_image", nullable = false)
    private boolean primaryImage = false;
}