package com.stockmanagement.stockmanagement.design.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DesignCreateRequest(

        @NotBlank(message = "Design name is required")
        @Size(
                max = 100,
                message = "Design name must not exceed 100 characters"
        )
        String designName,

        @NotNull(message = "Date of join is required")
        @PastOrPresent(
                message = "Date of join cannot be in the future"
        )
        LocalDate dateOfJoin
) {
}