package com.stockmanagement.stockmanagement.design.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DesignUpdateRequest(

        @NotBlank(message = "Design name is required")
        @Size(
                max = 100,
                message = "Design name must not exceed 100 characters"
        )
        String designName
) {
}