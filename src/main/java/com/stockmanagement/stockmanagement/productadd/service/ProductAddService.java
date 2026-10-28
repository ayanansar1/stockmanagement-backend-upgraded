package com.stockmanagement.stockmanagement.productadd.service;

import com.stockmanagement.stockmanagement.productadd.dto.ProductAddCreateRequest;
import com.stockmanagement.stockmanagement.productadd.dto.ProductAddResponse;

import java.util.List;

public interface ProductAddService {

    ProductAddResponse create(
            ProductAddCreateRequest request
    );

    ProductAddResponse getByCode(
            String productAddCode
    );

    List<ProductAddResponse> getAll();

    List<ProductAddResponse> getByKareegar(
            String kareegarCode
    );

    List<ProductAddResponse> getByMachine(
            String machineCode
    );
}