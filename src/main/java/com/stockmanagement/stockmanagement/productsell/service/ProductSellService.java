package com.stockmanagement.stockmanagement.productsell.service;

import com.stockmanagement.stockmanagement.productsell.dto.ProductSellResponse;
import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import com.stockmanagement.stockmanagement.salesinvoice.dto.AddInvoiceItemRequest;
import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoice;


import java.util.List;

public interface ProductSellService {

    ProductSell createForInvoice(
            SalesInvoice invoice,
            AddInvoiceItemRequest request
    );

    ProductSellResponse getByCode(
            String productSellCode
    );

    List<ProductSellResponse> getAll();

    List<ProductSellResponse> getByKareegar(
            String kareegarCode
    );

    List<ProductSellResponse> getByMachine(
            String machineCode
    );

    List<ProductSellResponse> getByBuyer(
            String buyerCode
    );
}