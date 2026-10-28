package com.stockmanagement.stockmanagement.productreturn.mapper;

import com.stockmanagement.stockmanagement.productreturn.dto.ProductReturnResponse;
import com.stockmanagement.stockmanagement.productreturn.entity.ProductReturn;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductReturnMapper {

    @Mapping(
            target = "productSellCode",
            source = "productSell.productSellCode"
    )

    @Mapping(target = "buyerCode", source = "productSell.buyer.buyerCode")
    @Mapping(target = "buyerName", source = "productSell.buyer.buyerName")

    @Mapping(
            target = "kareegarCode",
            source = "productSell.kareegar.kareegarCode"
    )
    @Mapping(
            target = "kareegarName",
            source = "productSell.kareegar.kareegarName"
    )

    @Mapping(target = "machineCode", source = "productSell.machine.machineCode")

    @Mapping(
            target = "productCode",
            source = "productSell.productKareegar.product.productCode"
    )
    @Mapping(
            target = "productName",
            source = "productSell.productKareegar.product.productName"
    )

    @Mapping(
            target = "banaCode",
            source = "productSell.banaKareegar.bana.banaCode"
    )
    @Mapping(
            target = "banaName",
            source = "productSell.banaKareegar.bana.banaName"
    )

    @Mapping(
            target = "designCode",
            source = "productSell.designKareegar.design.designCode"
    )
    @Mapping(
            target = "designName",
            source = "productSell.designKareegar.design.designName"
    )
    ProductReturnResponse toResponse(ProductReturn productReturn);
}