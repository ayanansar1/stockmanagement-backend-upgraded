package com.stockmanagement.stockmanagement.stockmovement.controller;

import com.stockmanagement.stockmanagement.stockmovement.dto.StockMovementResponse;
import com.stockmanagement.stockmanagement.stockmovement.enums.StockMovementType;
import com.stockmanagement.stockmanagement.stockmovement.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
@RequiredArgsConstructor
public class StockMovementController {

    private final StockMovementService stockMovementService;

    @GetMapping
    public ResponseEntity<List<StockMovementResponse>> getAll() {
        return ResponseEntity.ok(
                stockMovementService.getAll()
        );
    }

    @GetMapping("/{movementCode}")
    public ResponseEntity<StockMovementResponse> getByCode(
            @PathVariable String movementCode
    ) {
        return ResponseEntity.ok(
                stockMovementService.getByCode(movementCode)
        );
    }

    @GetMapping("/type/{movementType}")
    public ResponseEntity<List<StockMovementResponse>> getByType(
            @PathVariable StockMovementType movementType
    ) {
        return ResponseEntity.ok(
                stockMovementService.getByType(movementType)
        );
    }
}