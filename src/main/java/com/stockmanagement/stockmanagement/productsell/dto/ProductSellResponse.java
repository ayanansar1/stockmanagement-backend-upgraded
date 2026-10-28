package com.stockmanagement.stockmanagement.productsell.dto;

import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.enums.StockUnit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProductSellResponse(

        String productSellCode,

        String kareegarCode,
        String kareegarName,

        String machineCode,

        String productCode,
        String productName,

        String banaCode,
        String banaName,

        String designCode,
        String designName,

        String buyerCode,
        String buyerName,

        BigDecimal quantity,

        StockUnit unit,

        LocalDate sellDate,

        Status status,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {
}