package com.stockmanagement.stockmanagement.productreturn.service;

import com.stockmanagement.stockmanagement.productreturn.dto.ProductReturnCreateRequest;
import com.stockmanagement.stockmanagement.productreturn.dto.ProductReturnResponse;

import java.util.List;

public interface ProductReturnService {

    List<ProductReturnResponse> create(ProductReturnCreateRequest request);

    ProductReturnResponse getByCode(String productReturnCode);

    List<ProductReturnResponse> getAll();

    List<ProductReturnResponse> getByProductSell(String productSellCode);
}