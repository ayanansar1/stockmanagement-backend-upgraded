package com.stockmanagement.stockmanagement.product.mapper;

import com.stockmanagement.stockmanagement.product.dto.ProductCreateRequest;
import com.stockmanagement.stockmanagement.product.dto.ProductResponse;
import com.stockmanagement.stockmanagement.product.dto.ProductUpdateRequest;
import com.stockmanagement.stockmanagement.product.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCode", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Product toEntity(ProductCreateRequest request);


    ProductResponse toResponse(Product product);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productCode", ignore = true)
    @Mapping(target = "dateOfJoin", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntity(
            ProductUpdateRequest request,
            @MappingTarget Product product
    );
}