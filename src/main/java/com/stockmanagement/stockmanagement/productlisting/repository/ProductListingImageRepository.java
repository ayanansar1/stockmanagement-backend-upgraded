package com.stockmanagement.stockmanagement.productlisting.repository;

import com.stockmanagement.stockmanagement.productlisting.entity.ProductListingImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductListingImageRepository
        extends JpaRepository<ProductListingImage, Long> {

    List<ProductListingImage>
    findAllByListingIdAndDeletedFalseOrderByDisplayOrderAsc(Long listingId);

    Optional<ProductListingImage>
    findByIdAndListingIdAndDeletedFalse(Long id, Long listingId);
}