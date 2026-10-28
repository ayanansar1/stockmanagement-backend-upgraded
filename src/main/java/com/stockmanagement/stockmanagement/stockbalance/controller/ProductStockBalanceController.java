package com.stockmanagement.stockmanagement.stockbalance.controller;

import com.stockmanagement.stockmanagement.stockbalance.dto.ProductStockBalanceResponse;
import com.stockmanagement.stockmanagement.stockbalance.service.ProductStockBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-balances")
@RequiredArgsConstructor
public class ProductStockBalanceController {

    private final ProductStockBalanceService stockBalanceService;

    @GetMapping
    public ResponseEntity<List<ProductStockBalanceResponse>> getAll() {
        return ResponseEntity.ok(
                stockBalanceService.getAll()
        );
    }

    @GetMapping("/{stockBalanceCode}")
    public ResponseEntity<ProductStockBalanceResponse> getByCode(
            @PathVariable String stockBalanceCode
    ) {
        return ResponseEntity.ok(
                stockBalanceService.getByCode(stockBalanceCode)
        );
    }
}