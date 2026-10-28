package com.stockmanagement.stockmanagement.productadd.controller;

import com.stockmanagement.stockmanagement.productadd.dto.ProductAddCreateRequest;
import com.stockmanagement.stockmanagement.productadd.dto.ProductAddResponse;
import com.stockmanagement.stockmanagement.productadd.service.ProductAddService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-adds")
@RequiredArgsConstructor
public class ProductAddController {

    private final ProductAddService productAddService;


    @PostMapping
    public ResponseEntity<ProductAddResponse> create(
            @Valid @RequestBody
            ProductAddCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        productAddService.create(request)
                );
    }


    @GetMapping
    public ResponseEntity<List<ProductAddResponse>>
    getAll() {

        return ResponseEntity.ok(
                productAddService.getAll()
        );
    }


    @GetMapping("/{productAddCode}")
    public ResponseEntity<ProductAddResponse>
    getByCode(
            @PathVariable String productAddCode) {

        return ResponseEntity.ok(
                productAddService.getByCode(
                        productAddCode
                )
        );
    }


    @GetMapping("/kareegar/{kareegarCode}")
    public ResponseEntity<List<ProductAddResponse>>
    getByKareegar(
            @PathVariable String kareegarCode) {

        return ResponseEntity.ok(
                productAddService.getByKareegar(
                        kareegarCode
                )
        );
    }


    @GetMapping("/machine/{machineCode}")
    public ResponseEntity<List<ProductAddResponse>>
    getByMachine(
            @PathVariable String machineCode) {

        return ResponseEntity.ok(
                productAddService.getByMachine(
                        machineCode
                )
        );
    }
}