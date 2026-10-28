package com.stockmanagement.stockmanagement.product.dto;



import com.stockmanagement.stockmanagement.common.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProductResponse(

        String productCode,

        String productName,

        LocalDate dateOfJoin,

        Status status,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        Long version,

        boolean deleted,

        LocalDateTime deletedAt
) {
}
