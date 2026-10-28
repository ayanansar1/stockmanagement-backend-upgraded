package com.stockmanagement.stockmanagement.productlisting.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductListingUpdateRequest {

        @NotBlank
        @Size(max = 200)
        private String title;

        @NotBlank
        @Size(max = 220)
        @Pattern(
                regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$",
                message = "Slug must contain lowercase letters, numbers and hyphens"
        )
        private String slug;

        @Size(max = 10000)
        private String description;

        @NotNull
        @DecimalMin("0.01")
        @Digits(integer = 13, fraction = 2)
        private BigDecimal sellingPrice;

        @DecimalMin("0.001")
        @Digits(integer = 12, fraction = 3)
        private BigDecimal onlineStockLimit;

        /*
         * null: keep existing sources
         * []: clear sources
         * [1, 2]: replace sources
         */
        private List<@NotNull @Positive Long> stockBalanceIds;
}