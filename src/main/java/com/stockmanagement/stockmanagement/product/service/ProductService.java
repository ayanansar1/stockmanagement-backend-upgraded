package com.stockmanagement.stockmanagement.product.service;

import com.stockmanagement.stockmanagement.product.dto.ProductCreateRequest;
import com.stockmanagement.stockmanagement.product.dto.ProductResponse;
import com.stockmanagement.stockmanagement.product.dto.ProductUpdateRequest;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductCreateRequest request);

    ProductResponse getByCode(String productCode);

    List<ProductResponse> getAll();

    List<ProductResponse> getActive();

    List<ProductResponse> getInactive();

    ProductResponse update(
            String productCode,
            ProductUpdateRequest request
    );

    ProductResponse activate(String productCode);

    ProductResponse deactivate(String productCode);

    void softDelete(String productCode);

    ProductResponse restore(String productCode);

    List<ProductResponse> getDeleted();
}