package com.stockmanagement.stockmanagement.product.controller;

import com.stockmanagement.stockmanagement.product.dto.ProductCreateRequest;
import com.stockmanagement.stockmanagement.product.dto.ProductResponse;
import com.stockmanagement.stockmanagement.product.dto.ProductUpdateRequest;
import com.stockmanagement.stockmanagement.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @Valid @RequestBody
            ProductCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        productService.create(request)
                );
    }


    @GetMapping
    public List<ProductResponse> getAll() {

        return productService.getAll();
    }


    @GetMapping("/active")
    public List<ProductResponse> getActive() {

        return productService.getActive();
    }

    @GetMapping("/inactive")
    public List<ProductResponse> getInactive() {

        return productService.getInactive();
    }



    @GetMapping("/deleted")
    public List<ProductResponse> getDeleted() {

        return productService.getDeleted();
    }


    @GetMapping("/{productCode}")
    public ProductResponse getByCode(
            @PathVariable String productCode) {

        return productService.getByCode(
                productCode
        );
    }


    @PutMapping("/{productCode}")
    public ProductResponse update(
            @PathVariable String productCode,
            @Valid @RequestBody
            ProductUpdateRequest request) {

        return productService.update(
                productCode,
                request
        );
    }


    @PatchMapping("/{productCode}/activate")
    public ProductResponse activate(
            @PathVariable String productCode) {

        return productService.activate(
                productCode
        );
    }


    @PatchMapping("/{productCode}/deactivate")
    public ProductResponse deactivate(
            @PathVariable String productCode) {

        return productService.deactivate(
                productCode
        );
    }


    @DeleteMapping("/{productCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable String productCode) {

        productService.softDelete(
                productCode
        );
    }


    @PatchMapping("/{productCode}/restore")
    public ProductResponse restore(
            @PathVariable String productCode) {

        return productService.restore(
                productCode
        );
    }
}