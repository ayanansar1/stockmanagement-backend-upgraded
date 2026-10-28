package com.stockmanagement.stockmanagement.salesinvoice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record CreateInvoiceRequest(

        @NotBlank(message = "Buyer code is required")
        String buyerCode,

        @NotNull(message = "Invoice date is required")
        @PastOrPresent(
                message = "Invoice date cannot be in the future"
        )
        LocalDate invoiceDate
) {
}