package com.stockmanagement.stockmanagement.productadd.dto;

import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.enums.StockUnit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProductAddResponse(

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

        BigDecimal quantity,
        StockUnit unit,

        LocalDate addedDate,

        Status status,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        Long version
) {
}