package com.stockmanagement.stockmanagement.mapping.productKareegar.dto;

import com.stockmanagement.stockmanagement.common.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProductKareegarResponse(

        String productKareegarCode,

        String kareegarCode,
        String kareegarName,

        String productCode,
        String productName,

        String machineCode,

        Status status,
        LocalDate assignedDate,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version,
        boolean deleted,
        LocalDateTime deletedAt
) {
}