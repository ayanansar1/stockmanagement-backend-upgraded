package com.stockmanagement.stockmanagement.productlisting.dto;

import com.stockmanagement.stockmanagement.productlisting.enums.ListingType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductListingCardResponse {

    private String listingCode;
    private ListingType listingType;
    private String title;
    private String slug;
    private BigDecimal sellingPrice;
    private String primaryImageUrl;
}