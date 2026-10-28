package com.stockmanagement.stockmanagement.mapping.productKareegar.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record ProductKareegarUpdateRequest(

        @NotBlank(message = "Kareegar code is required")
        String kareegarCode,

        @NotBlank(message = "Product code is required")
        String productCode,

        @NotBlank(message = "Machine code is required")
        String machineCode,

        @NotNull(message = "Assigned date is required")
        @PastOrPresent(
                message = "Assigned date cannot be in the future"
        )
        LocalDate assignedDate
) {
}