package com.stockmanagement.stockmanagement.mapping.designKareegar.dto;

import com.stockmanagement.stockmanagement.common.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DesignKareegarResponse(

        String designKareegarCode,

        String kareegarCode,
        String kareegarName,

        String designCode,
        String designName,

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