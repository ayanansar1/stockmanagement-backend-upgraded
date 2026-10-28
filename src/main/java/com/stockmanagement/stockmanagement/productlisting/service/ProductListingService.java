package com.stockmanagement.stockmanagement.productlisting.service;

import com.stockmanagement.stockmanagement.productlisting.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface ProductListingService {

    ProductListingResponse create(ProductListingCreateRequest request);

    ProductListingResponse update(
            String listingCode,
            ProductListingUpdateRequest request
    );

    ProductListingResponse publish(String listingCode);

    ProductListingResponse unpublish(String listingCode);

    ProductListingResponse getAdminByCode(String listingCode);

    Page<ProductListingResponse> getAllAdmin(Pageable pageable);

    Page<ProductListingCardResponse> getPublished(
            String keyword,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );

    ProductListingResponse getPublishedByCode(String listingCode);
}