package com.stockmanagement.stockmanagement.productlisting.repository;

import com.stockmanagement.stockmanagement.productlisting.entity.ProductListing;
import com.stockmanagement.stockmanagement.productlisting.enums.ListingStatus;
import com.stockmanagement.stockmanagement.productlisting.enums.ListingType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductListingRepository
        extends JpaRepository<ProductListing, Long> {

    Optional<ProductListing> findByListingCodeAndDeletedFalse(
            String listingCode
    );

    boolean existsBySlugAndDeletedFalse(String slug);

    boolean existsBySlugAndIdNotAndDeletedFalse(
            String slug,
            Long id
    );

    @Query("""
        SELECT l
        FROM ProductListing l
        JOIN FETCH l.product
        WHERE l.listingCode = :code
          AND l.deleted = false
    """)
    Optional<ProductListing> findAdminDetailByCode(
            @Param("code") String code
    );

    Page<ProductListing> findAllByDeletedFalse(Pageable pageable);

    @Query("""
        SELECT l
        FROM ProductListing l
        JOIN FETCH l.product
        WHERE l.deleted = false
          AND l.listingStatus = :status
          AND l.listingType = :type
          AND l.product.deleted = false
          AND (
              :keyword IS NULL
              OR LOWER(l.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR LOWER(l.product.productName)
                    LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
          AND (:minPrice IS NULL OR l.sellingPrice >= :minPrice)
          AND (:maxPrice IS NULL OR l.sellingPrice <= :maxPrice)
    """)
    Page<ProductListing> searchPublished(
            @Param("status") ListingStatus status,
            @Param("type") ListingType type,
            @Param("keyword") String keyword,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );

    @Query("""
        SELECT l
        FROM ProductListing l
        JOIN FETCH l.product
        WHERE l.listingCode = :code
          AND l.deleted = false
          AND l.listingStatus = :status
          AND l.listingType = :type
          AND l.product.deleted = false
    """)
    Optional<ProductListing> findPublishedDetail(
            @Param("code") String code,
            @Param("status") ListingStatus status,
            @Param("type") ListingType type
    );
}