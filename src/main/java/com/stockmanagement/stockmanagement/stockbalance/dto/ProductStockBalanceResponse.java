package com.stockmanagement.stockmanagement.stockbalance.dto;


import com.stockmanagement.stockmanagement.common.enums.StockUnit;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductStockBalanceResponse(

        String stockBalanceCode,

        String kareegarCode,
        String kareegarName,

        String machineCode,

        String productCode,
        String productName,

        String banaCode,
        String banaName,

        String designCode,
        String designName,

        BigDecimal quantity,

        StockUnit unit,

        LocalDateTime updatedAt,

        Long version
) {
}