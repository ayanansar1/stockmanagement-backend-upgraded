package com.stockmanagement.stockmanagement.product.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProductCreateRequest(

        @NotBlank(message = "Product name is required")
        @Size(
                max = 100,
                message = "Product name must not exceed 100 characters"
        )
        String productName,

        @NotNull(message = "Date of join is required")
        @PastOrPresent(
                message = "Date of join cannot be in the future"
        )
        LocalDate dateOfJoin
) {
}