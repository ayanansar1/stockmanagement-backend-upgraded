package com.stockmanagement.stockmanagement.mapping.banaKareegar.dto;

import com.stockmanagement.stockmanagement.common.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record BanaKareegarResponse(

        String banaKareegarCode,

        String kareegarCode,
        String kareegarName,

        String banaCode,
        String banaName,

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