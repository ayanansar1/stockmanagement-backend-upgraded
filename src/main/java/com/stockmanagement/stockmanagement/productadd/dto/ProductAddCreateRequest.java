package com.stockmanagement.stockmanagement.productadd.dto;

import com.stockmanagement.stockmanagement.common.enums.StockUnit;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductAddCreateRequest(

        @NotBlank(message = "Kareegar code is required")
        String kareegarCode,

        @NotBlank(message = "Machine code is required")
        String machineCode,

        @NotBlank(message = "Product code is required")
        String productCode,

        @NotBlank(message = "Bana code is required")
        String banaCode,

        @NotBlank(message = "Design code is required")
        String designCode,

        @NotNull(message = "Quantity is required")
        @DecimalMin(
                value = "0.001",
                message = "Quantity must be greater than zero"
        )
        BigDecimal quantity,

        @NotNull(message = "Unit is required")
        StockUnit unit,

        @NotNull(message = "Added date is required")
        @PastOrPresent(
                message = "Added date cannot be in the future"
        )
        LocalDate addedDate
) {
}