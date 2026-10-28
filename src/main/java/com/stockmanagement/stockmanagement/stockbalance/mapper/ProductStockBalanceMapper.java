package com.stockmanagement.stockmanagement.stockbalance.mapper;

import com.stockmanagement.stockmanagement.stockbalance.dto.ProductStockBalanceResponse;
import com.stockmanagement.stockmanagement.stockbalance.entity.ProductStockBalance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductStockBalanceMapper {

    @Mapping(target = "kareegarCode",
            source = "kareegar.kareegarCode")

    @Mapping(target = "kareegarName",
            source = "kareegar.kareegarName")

    @Mapping(target = "machineCode",
            source = "machine.machineCode")

    @Mapping(target = "productCode",
            source = "productKareegar.product.productCode")

    @Mapping(target = "productName",
            source = "productKareegar.product.productName")

    @Mapping(target = "banaCode",
            source = "banaKareegar.bana.banaCode")

    @Mapping(target = "banaName",
            source = "banaKareegar.bana.banaName")

    @Mapping(target = "designCode",
            source = "designKareegar.design.designCode")

    @Mapping(target = "designName",
            source = "designKareegar.design.designName")

    ProductStockBalanceResponse toResponse(ProductStockBalance entity);
}