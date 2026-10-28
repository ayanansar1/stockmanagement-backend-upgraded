package com.stockmanagement.stockmanagement.productlisting.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductListingImageResponse {

    private String imageUrl;
    private String altText;
    private Integer displayOrder;
    private boolean primaryImage;
}