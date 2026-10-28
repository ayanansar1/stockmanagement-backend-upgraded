package com.stockmanagement.stockmanagement.stockmovement.dto;

import com.stockmanagement.stockmanagement.common.enums.StockUnit;
import com.stockmanagement.stockmanagement.stockmovement.enums.StockMovementType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record StockMovementResponse(

        String movementCode,

        String productAddCode,

        String kareegarCode,
        String kareegarName,

        String machineCode,

        String productCode,
        String productName,

        String banaCode,
        String banaName,

        String designCode,
        String designName,

        StockMovementType movementType,

        BigDecimal quantity,

        StockUnit unit,

        LocalDate movementDate,

        LocalDateTime createdAt
) {
}