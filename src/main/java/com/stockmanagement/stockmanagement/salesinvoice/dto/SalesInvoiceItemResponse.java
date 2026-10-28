package com.stockmanagement.stockmanagement.salesinvoice.dto;


import com.stockmanagement.stockmanagement.common.enums.StockUnit;

import java.math.BigDecimal;

public record SalesInvoiceItemResponse(

        String productSellCode,

        String productCode,
        String productName,

        String banaCode,
        String banaName,

        String designCode,
        String designName,

        BigDecimal quantity,

        StockUnit unit,

        BigDecimal rate,

        BigDecimal amount
) {
}