package com.stockmanagement.stockmanagement.productlisting.mapper;

import com.stockmanagement.stockmanagement.productlisting.dto.*;
import com.stockmanagement.stockmanagement.productlisting.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductListingMapper {

    @Mapping(target = "productCode", source = "product.productCode")
    @Mapping(target = "productName", source = "product.productName")
    @Mapping(target = "images", ignore = true)
    ProductListingResponse toResponse(ProductListing listing);

    ProductListingImageResponse toImageResponse(
            ProductListingImage image
    );

    @Mapping(target = "primaryImageUrl", ignore = true)
    ProductListingCardResponse toCardResponse(ProductListing listing);
}