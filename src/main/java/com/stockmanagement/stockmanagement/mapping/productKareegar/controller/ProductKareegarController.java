package com.stockmanagement.stockmanagement.mapping.productKareegar.controller;

import com.stockmanagement.stockmanagement.mapping.productKareegar.dto.ProductKareegarCreateRequest;
import com.stockmanagement.stockmanagement.mapping.productKareegar.dto.ProductKareegarResponse;
import com.stockmanagement.stockmanagement.mapping.productKareegar.dto.ProductKareegarUpdateRequest;
import com.stockmanagement.stockmanagement.mapping.productKareegar.service.ProductKareegarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-kareegars")
@RequiredArgsConstructor
public class ProductKareegarController {

    private final ProductKareegarService productKareegarService;


    // CREATE
    @PostMapping
    public ResponseEntity<ProductKareegarResponse> create(
            @Valid @RequestBody ProductKareegarCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productKareegarService.create(request));
    }


    // GET ALL
    @GetMapping
    public ResponseEntity<List<ProductKareegarResponse>> getAll() {

        return ResponseEntity.ok(
                productKareegarService.getAll()
        );
    }


    // GET ACTIVE
    @GetMapping("/active")
    public ResponseEntity<List<ProductKareegarResponse>> getActive() {

        return ResponseEntity.ok(
                productKareegarService.getActive()
        );
    }


    // GET DELETED
    @GetMapping("/deleted")
    public ResponseEntity<List<ProductKareegarResponse>> getDeleted() {

        return ResponseEntity.ok(
                productKareegarService.getDeleted()
        );
    }


    // GET BY KAREEGAR
    @GetMapping("/kareegar/{kareegarCode}")
    public ResponseEntity<List<ProductKareegarResponse>> getByKareegar(
            @PathVariable String kareegarCode) {

        return ResponseEntity.ok(
                productKareegarService.getByKareegar(
                        kareegarCode
                )
        );
    }


    // GET BY PRODUCT-KAREEGAR CODE
    @GetMapping("/{productKareegarCode}")
    public ResponseEntity<ProductKareegarResponse> getByCode(
            @PathVariable String productKareegarCode) {

        return ResponseEntity.ok(
                productKareegarService.getByCode(
                        productKareegarCode
                )
        );
    }


    // UPDATE
    @PutMapping("/{productKareegarCode}")
    public ResponseEntity<ProductKareegarResponse> update(
            @PathVariable String productKareegarCode,
            @Valid @RequestBody ProductKareegarUpdateRequest request) {

        return ResponseEntity.ok(
                productKareegarService.update(
                        productKareegarCode,
                        request
                )
        );
    }


    // ACTIVATE
    @PatchMapping("/{productKareegarCode}/activate")
    public ResponseEntity<ProductKareegarResponse> activate(
            @PathVariable String productKareegarCode) {

        return ResponseEntity.ok(
                productKareegarService.activate(
                        productKareegarCode
                )
        );
    }


    // DEACTIVATE
    @PatchMapping("/{productKareegarCode}/deactivate")
    public ResponseEntity<ProductKareegarResponse> deactivate(
            @PathVariable String productKareegarCode) {

        return ResponseEntity.ok(
                productKareegarService.deactivate(
                        productKareegarCode
                )
        );
    }


    // SOFT DELETE
    @DeleteMapping("/{productKareegarCode}")
    public ResponseEntity<Void> delete(
            @PathVariable String productKareegarCode) {

        productKareegarService.delete(
                productKareegarCode
        );

        return ResponseEntity.noContent().build();
    }


    // RESTORE
    @PatchMapping("/{productKareegarCode}/restore")
    public ResponseEntity<ProductKareegarResponse> restore(
            @PathVariable String productKareegarCode) {

        return ResponseEntity.ok(
                productKareegarService.restore(
                        productKareegarCode
                )
        );
    }
}