package com.stockmanagement.stockmanagement.mapping.productKareegar.mapper;

import com.stockmanagement.stockmanagement.mapping.productKareegar.dto.ProductKareegarResponse;
import com.stockmanagement.stockmanagement.mapping.productKareegar.entity.ProductKareegar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductKareegarMapper {

    @Mapping(
            target = "kareegarCode",
            source = "kareegar.kareegarCode"
    )
    @Mapping(
            target = "kareegarName",
            source = "kareegar.kareegarName"
    )
    @Mapping(
            target = "productCode",
            source = "product.productCode"
    )
    @Mapping(
            target = "productName",
            source = "product.productName"
    )
    @Mapping(
            target = "machineCode",
            source = "machine.machineCode"
    )
    ProductKareegarResponse toResponse(ProductKareegar entity);
}