package com.stockmanagement.stockmanagement.salesinvoice.mapper;

import com.stockmanagement.stockmanagement.salesinvoice.dto.SalesInvoiceItemResponse;
import com.stockmanagement.stockmanagement.salesinvoice.dto.SalesInvoiceResponse;
import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoice;
import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoiceItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SalesInvoiceMapper {

    @Mapping(
            target = "buyerCode",
            source = "buyer.buyerCode"
    )
    @Mapping(
            target = "buyerName",
            source = "buyer.buyerName"
    )
    SalesInvoiceResponse toResponse(
            SalesInvoice invoice
    );

    @Mapping(
            target = "productSellCode",
            source = "productSell.productSellCode"
    )
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
    SalesInvoiceItemResponse toItemResponse(
            SalesInvoiceItem item
    );
}