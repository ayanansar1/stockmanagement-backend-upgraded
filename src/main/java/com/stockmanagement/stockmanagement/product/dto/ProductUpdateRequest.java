package com.stockmanagement.stockmanagement.product.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(

        @NotBlank(message = "Product name is required")
        @Size(
                max = 100,
                message = "Product name must not exceed 100 characters"
        )
        String productName
) {
}
