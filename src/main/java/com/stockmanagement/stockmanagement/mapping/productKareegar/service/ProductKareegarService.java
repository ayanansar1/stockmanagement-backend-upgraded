package com.stockmanagement.stockmanagement.mapping.productKareegar.service;

import com.stockmanagement.stockmanagement.mapping.productKareegar.dto.ProductKareegarCreateRequest;
import com.stockmanagement.stockmanagement.mapping.productKareegar.dto.ProductKareegarResponse;
import com.stockmanagement.stockmanagement.mapping.productKareegar.dto.ProductKareegarUpdateRequest;

import java.util.List;

public interface ProductKareegarService {

    ProductKareegarResponse create(
            ProductKareegarCreateRequest request
    );

    ProductKareegarResponse getByCode(
            String productKareegarCode
    );

    List<ProductKareegarResponse> getAll();

    List<ProductKareegarResponse> getActive();

    List<ProductKareegarResponse> getDeleted();

    List<ProductKareegarResponse> getByKareegar(
            String kareegarCode
    );

    ProductKareegarResponse update(
            String productKareegarCode,
            ProductKareegarUpdateRequest request
    );

    ProductKareegarResponse activate(
            String productKareegarCode
    );

    ProductKareegarResponse deactivate(
            String productKareegarCode
    );

    void delete(
            String productKareegarCode
    );

    ProductKareegarResponse restore(
            String productKareegarCode
    );
}