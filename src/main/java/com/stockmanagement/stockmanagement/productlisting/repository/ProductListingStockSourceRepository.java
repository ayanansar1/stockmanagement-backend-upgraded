package com.stockmanagement.stockmanagement.productlisting.repository;

import com.stockmanagement.stockmanagement.productlisting.entity.ProductListingStockSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductListingStockSourceRepository
        extends JpaRepository<ProductListingStockSource, Long> {

    List<ProductListingStockSource>
    findAllByListingIdAndEnabledTrueAndDeletedFalse(Long listingId);

    List<ProductListingStockSource>
    findAllByListingIdAndDeletedFalse(Long listingId);
}