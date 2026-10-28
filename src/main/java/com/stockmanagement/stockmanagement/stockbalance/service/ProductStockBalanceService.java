package com.stockmanagement.stockmanagement.stockbalance.service;

import com.stockmanagement.stockmanagement.productadd.entity.ProductAdd;
import com.stockmanagement.stockmanagement.productreturn.entity.ProductReturn;
import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import com.stockmanagement.stockmanagement.stockbalance.dto.ProductStockBalanceResponse;

import java.util.List;

public interface ProductStockBalanceService {

    ProductStockBalanceResponse addStock(
            ProductAdd productAdd
    );

    ProductStockBalanceResponse deductStock(
            ProductSell productSell
    );

    ProductStockBalanceResponse addReturnedStock(ProductReturn productReturn);

    List<ProductStockBalanceResponse> getAll();

    ProductStockBalanceResponse getByCode(String stockBalanceCode);
}