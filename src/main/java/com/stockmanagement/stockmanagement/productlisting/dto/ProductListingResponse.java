package com.stockmanagement.stockmanagement.productlisting.dto;

import com.stockmanagement.stockmanagement.productlisting.enums.ListingStatus;
import com.stockmanagement.stockmanagement.productlisting.enums.ListingType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProductListingResponse {

    private String listingCode;
    private String productCode;
    private String productName;

    private ListingType listingType;
    private ListingStatus listingStatus;

    private String title;
    private String slug;
    private String description;

    private BigDecimal sellingPrice;
    private BigDecimal onlineStockLimit;

    private List<ProductListingImageResponse> images;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;
}