package com.stockmanagement.stockmanagement.productsell.controller;

import com.stockmanagement.stockmanagement.productsell.dto.ProductSellResponse;
import com.stockmanagement.stockmanagement.productsell.service.ProductSellService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-sells")
@RequiredArgsConstructor
public class ProductSellController {

    private final ProductSellService productSellService;

    @GetMapping
    public ResponseEntity<List<ProductSellResponse>> getAll() {

        return ResponseEntity.ok(
                productSellService.getAll()
        );
    }

    @GetMapping("/{productSellCode}")
    public ResponseEntity<ProductSellResponse> getByCode(
            @PathVariable String productSellCode
    ) {

        return ResponseEntity.ok(
                productSellService.getByCode(productSellCode)
        );
    }

    @GetMapping("/kareegar/{kareegarCode}")
    public ResponseEntity<List<ProductSellResponse>> getByKareegar(
            @PathVariable String kareegarCode
    ) {

        return ResponseEntity.ok(
                productSellService.getByKareegar(
                        kareegarCode
                )
        );
    }

    @GetMapping("/machine/{machineCode}")
    public ResponseEntity<List<ProductSellResponse>> getByMachine(
            @PathVariable String machineCode
    ) {

        return ResponseEntity.ok(
                productSellService.getByMachine(
                        machineCode
                )
        );
    }

    @GetMapping("/buyer/{buyerCode}")
    public ResponseEntity<List<ProductSellResponse>> getByBuyer(
            @PathVariable String buyerCode
    ) {

        return ResponseEntity.ok(
                productSellService.getByBuyer(
                        buyerCode
                )
        );
    }
}