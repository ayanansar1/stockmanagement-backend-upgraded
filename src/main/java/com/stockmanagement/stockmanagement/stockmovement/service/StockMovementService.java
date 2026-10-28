package com.stockmanagement.stockmanagement.stockmovement.service;

import com.stockmanagement.stockmanagement.productadd.entity.ProductAdd;
import com.stockmanagement.stockmanagement.productreturn.entity.ProductReturn;
import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import com.stockmanagement.stockmanagement.stockmovement.dto.StockMovementResponse;
import com.stockmanagement.stockmanagement.stockmovement.enums.StockMovementType;

import java.util.List;

public interface StockMovementService {

    // Product Add → IN
    StockMovementResponse createInMovement(
            ProductAdd productAdd
    );

    // Product Sell → OUT
    StockMovementResponse createOutMovement(
            ProductSell productSell
    );

    StockMovementResponse createReturnMovement(ProductReturn productReturn);

    StockMovementResponse getByCode(
            String movementCode
    );

    List<StockMovementResponse> getAll();

    List<StockMovementResponse> getByType(
            StockMovementType movementType
    );


}