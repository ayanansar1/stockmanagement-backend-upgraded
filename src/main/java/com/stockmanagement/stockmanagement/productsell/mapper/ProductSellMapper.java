package com.stockmanagement.stockmanagement.productsell.mapper;

import com.stockmanagement.stockmanagement.productsell.dto.ProductSellResponse;
import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductSellMapper {

    @Mapping(
            target = "kareegarCode",
            source = "kareegar.kareegarCode"
    )
    @Mapping(
            target = "kareegarName",
            source = "kareegar.kareegarName"
    )
    @Mapping(
            target = "machineCode",
            source = "machine.machineCode"
    )
    @Mapping(
            target = "productCode",
            source = "productKareegar.product.productCode"
    )
    @Mapping(
            target = "productName",
            source = "productKareegar.product.productName"
    )
    @Mapping(
            target = "banaCode",
            source = "banaKareegar.bana.banaCode"
    )
    @Mapping(
            target = "banaName",
            source = "banaKareegar.bana.banaName"
    )
    @Mapping(
            target = "designCode",
            source = "designKareegar.design.designCode"
    )
    @Mapping(
            target = "designName",
            source = "designKareegar.design.designName"
    )
    @Mapping(
            target = "buyerCode",
            source = "buyer.buyerCode"
    )
    @Mapping(
            target = "buyerName",
            source = "buyer.buyerName"
    )
    ProductSellResponse toResponse(ProductSell entity);
}