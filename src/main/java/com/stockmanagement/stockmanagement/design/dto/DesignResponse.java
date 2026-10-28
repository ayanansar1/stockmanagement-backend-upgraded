package com.stockmanagement.stockmanagement.design.dto;



import com.stockmanagement.stockmanagement.common.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DesignResponse(

        String designCode,

        String designName,

        LocalDate dateOfJoin,

        Status status,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        Long version,

        boolean deleted,

        LocalDateTime deletedAt
) {
}