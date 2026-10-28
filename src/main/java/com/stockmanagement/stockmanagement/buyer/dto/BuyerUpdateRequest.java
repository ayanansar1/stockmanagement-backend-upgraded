package com.stockmanagement.stockmanagement.buyer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BuyerUpdateRequest(

        @NotBlank(message = "Buyer name is required")
        @Size(
                max = 100,
                message = "Buyer name must not exceed 100 characters"
        )
        String buyerName,

        @Size(
                max = 150,
                message = "Firm name must not exceed 150 characters"
        )
        String firmName,

        @NotBlank(message = "Mobile number is required")
        @Size(
                max = 15,
                message = "Mobile number must not exceed 15 characters"
        )
        String mobileNo,

        @Email(message = "Invalid email format")
        @Size(
                max = 150,
                message = "Email must not exceed 150 characters"
        )
        String emailId,

        @Size(
                max = 255,
                message = "Address must not exceed 255 characters"
        )
        String address
) {
}