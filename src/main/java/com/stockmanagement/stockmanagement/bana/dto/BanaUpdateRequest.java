package com.stockmanagement.stockmanagement.bana.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BanaUpdateRequest(

        @NotBlank(message = "Bana name is required")
        @Size(max = 100, message = "Bana name must not exceed 100 characters")
        String banaName
) {
}
