package com.stockmanagement.stockmanagement.productlisting.dto;

import com.stockmanagement.stockmanagement.productlisting.enums.ListingType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ProductListingCreateRequest {

        @NotBlank
        @Size(max = 30)
        private String productCode;

        @NotNull
        private ListingType listingType = ListingType.ONLINE_STORE;

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

        private List<@NotNull @Positive Long> stockBalanceIds;
}