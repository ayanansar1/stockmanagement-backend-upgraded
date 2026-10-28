package com.stockmanagement.stockmanagement.stockmovement.mapper;

import com.stockmanagement.stockmanagement.stockmovement.dto.StockMovementResponse;
import com.stockmanagement.stockmanagement.stockmovement.entity.StockMovement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {

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

    StockMovementResponse toResponse(StockMovement entity);
}