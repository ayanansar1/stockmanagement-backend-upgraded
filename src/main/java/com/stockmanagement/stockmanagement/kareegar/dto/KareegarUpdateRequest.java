package com.stockmanagement.stockmanagement.kareegar.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record KareegarUpdateRequest(

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
        String address
) {
}
