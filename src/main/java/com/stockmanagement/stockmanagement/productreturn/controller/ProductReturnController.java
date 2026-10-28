package com.stockmanagement.stockmanagement.productreturn.controller;

import com.stockmanagement.stockmanagement.productreturn.dto.ProductReturnCreateRequest;
import com.stockmanagement.stockmanagement.productreturn.dto.ProductReturnResponse;
import com.stockmanagement.stockmanagement.productreturn.service.ProductReturnService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-returns")
@RequiredArgsConstructor
public class ProductReturnController {

    private final ProductReturnService productReturnService;

    @PostMapping
    public ResponseEntity<List<ProductReturnResponse>> create(
            @Valid @RequestBody ProductReturnCreateRequest request) {

        List<ProductReturnResponse> response =
                productReturnService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductReturnResponse>> getAll() {

        return ResponseEntity.ok(
                productReturnService.getAll()
        );
    }

    @GetMapping("/{productReturnCode}")
    public ResponseEntity<ProductReturnResponse> getByCode(
            @PathVariable String productReturnCode) {

        return ResponseEntity.ok(
                productReturnService.getByCode(productReturnCode)
        );
    }

    @GetMapping("/product-sells/{productSellCode}")
    public ResponseEntity<List<ProductReturnResponse>> getByProductSell(
            @PathVariable String productSellCode) {

        return ResponseEntity.ok(
                productReturnService.getByProductSell(productSellCode)
        );
    }
}