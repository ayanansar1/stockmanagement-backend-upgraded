package com.stockmanagement.stockmanagement.kareegar.dto;



import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record KareegarCreateRequest(

        @NotBlank(message = "Kareegar name is required")
        @Size(max = 100)
        String kareegarName,

        @NotBlank(message = "Mobile number is required")
        @Size(max = 15)
        String mobileNo,

        @Email(message = "Invalid email")
        @Size(max = 150)
        String emailId,

        @Size(max = 255)
        String address,

        @NotNull(message = "Join date is required")
        @PastOrPresent(message = "Join date cannot be in the future")
        LocalDate joinDate
) {
}
