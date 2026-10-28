package com.stockmanagement.stockmanagement.productreturn.dto;

import com.stockmanagement.stockmanagement.productreturn.enums.ReturnReason;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProductReturnCreateRequest(

        @NotBlank(message = "Invoice code is required")
        String invoiceCode,

        @NotNull(message = "Return date is required")
        @PastOrPresent(message = "Return date cannot be in the future")
        LocalDate returnDate,

        @NotNull(message = "Return reason is required")
        ReturnReason returnReason,

        @Size(max = 500, message = "Remarks cannot exceed 500 characters")
        String remarks,

        @NotEmpty(message = "At least one return item is required")
        @Size(max = 100, message = "Maximum 100 items allowed per request")
        List<@NotNull @Valid ProductReturnItemRequest> items

) {

        public record ProductReturnItemRequest(

                @NotBlank(message = "Product sell code is required")
                String productSellCode,

                @NotNull(message = "Return quantity is required")
                @DecimalMin(
                        value = "0.001",
                        message = "Return quantity must be greater than zero"
                )
                @Digits(
                        integer = 12,
                        fraction = 3,
                        message = "Quantity can have at most 3 decimal places"
                )
                BigDecimal returnQuantity

        ) {}
}