package com.stockmanagement.stockmanagement.productlisting.controller;

import com.stockmanagement.stockmanagement.productlisting.dto.*;
import com.stockmanagement.stockmanagement.productlisting.service.ProductListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductListingController {

    private final ProductListingService listingService;

    // ---------------- ADMIN APIs ----------------

    @PostMapping("/admin/product-listings")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductListingResponse create(
            @Valid @RequestBody ProductListingCreateRequest request) {

        return listingService.create(request);
    }

    @PutMapping("/admin/product-listings/{listingCode}")
    public ProductListingResponse update(
            @PathVariable String listingCode,
            @Valid @RequestBody ProductListingUpdateRequest request) {

        return listingService.update(listingCode, request);
    }

    @PatchMapping("/admin/product-listings/{listingCode}/publish")
    public ProductListingResponse publish(
            @PathVariable String listingCode) {

        return listingService.publish(listingCode);
    }

    @PatchMapping("/admin/product-listings/{listingCode}/unpublish")
    public ProductListingResponse unpublish(
            @PathVariable String listingCode) {

        return listingService.unpublish(listingCode);
    }

    @GetMapping("/admin/product-listings/{listingCode}")
    public ProductListingResponse getAdminByCode(
            @PathVariable String listingCode) {

        return listingService.getAdminByCode(listingCode);
    }

    @GetMapping("/admin/product-listings")
    public Page<ProductListingResponse> getAllAdmin(
            @PageableDefault(
                    size = 20,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        return listingService.getAllAdmin(pageable);
    }

    // ---------------- PUBLIC STORE APIs ----------------

    @GetMapping("/store/products")
    public Page<ProductListingCardResponse> getPublished(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(
                    size = 20,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC
            ) Pageable pageable) {

        return listingService.getPublished(
                keyword,
                minPrice,
                maxPrice,
                pageable
        );
    }

    @GetMapping("/store/products/{listingCode}")
    public ProductListingResponse getPublishedByCode(
            @PathVariable String listingCode) {

        return listingService.getPublishedByCode(listingCode);
    }
}