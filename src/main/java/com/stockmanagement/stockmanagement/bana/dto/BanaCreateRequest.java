package com.stockmanagement.stockmanagement.bana.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record BanaCreateRequest(

        @NotBlank(message = "Bana name is required")
        @Size(max = 100, message = "Bana name must not exceed 100 characters")
        String banaName,

        @NotNull(message = "Date of join is required")
        @PastOrPresent(message = "Date of join cannot be in the future")
        LocalDate dateOfJoin
) {
}