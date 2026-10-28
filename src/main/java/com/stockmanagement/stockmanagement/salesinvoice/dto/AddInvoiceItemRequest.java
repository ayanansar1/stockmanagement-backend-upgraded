package com.stockmanagement.stockmanagement.salesinvoice.dto;


import com.stockmanagement.stockmanagement.common.enums.StockUnit;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AddInvoiceItemRequest(

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

        @NotNull(message = "Rate is required")
        @DecimalMin(
                value = "0.01",
                message = "Rate must be greater than zero"
        )
        BigDecimal rate,

        @NotNull(message = "Sell date is required")
        @PastOrPresent(
                message = "Sell date cannot be in the future"
        )
        LocalDate sellDate
) {
}