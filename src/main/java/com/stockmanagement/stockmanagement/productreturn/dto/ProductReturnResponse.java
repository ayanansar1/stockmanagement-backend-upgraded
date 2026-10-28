package com.stockmanagement.stockmanagement.productreturn.dto;

import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.enums.StockUnit;
import com.stockmanagement.stockmanagement.productreturn.enums.ReturnReason;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProductReturnResponse(

        String productReturnCode,

        String productSellCode,

        String buyerCode,
        String buyerName,

        String kareegarCode,
        String kareegarName,

        String machineCode,

        String productCode,
        String productName,

        String banaCode,
        String banaName,

        String designCode,
        String designName,

        BigDecimal returnQuantity,
        StockUnit unit,

        LocalDate returnDate,
        ReturnReason returnReason,
        String remarks,

        Status status,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version

) {}