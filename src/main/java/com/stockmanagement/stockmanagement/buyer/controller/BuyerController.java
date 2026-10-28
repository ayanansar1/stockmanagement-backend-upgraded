package com.stockmanagement.stockmanagement.buyer.controller;

import com.stockmanagement.stockmanagement.buyer.dto.BuyerCreateRequest;
import com.stockmanagement.stockmanagement.buyer.dto.BuyerResponse;
import com.stockmanagement.stockmanagement.buyer.dto.BuyerUpdateRequest;
import com.stockmanagement.stockmanagement.buyer.service.BuyerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buyers")
@RequiredArgsConstructor
public class BuyerController {

    private final BuyerService buyerService;

    @PostMapping
    public ResponseEntity<BuyerResponse> create(
            @Valid @RequestBody BuyerCreateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(buyerService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<BuyerResponse>> getAll() {
        return ResponseEntity.ok(
                buyerService.getAll()
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<BuyerResponse>> getActive() {
        return ResponseEntity.ok(
                buyerService.getActive()
        );
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<BuyerResponse>> getInactive() {
        return ResponseEntity.ok(
                buyerService.getInactive()
        );
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<BuyerResponse>> getDeleted() {
        return ResponseEntity.ok(
                buyerService.getDeleted()
        );
    }

    @GetMapping("/{buyerCode}")
    public ResponseEntity<BuyerResponse> getByCode(
            @PathVariable String buyerCode
    ) {
        return ResponseEntity.ok(
                buyerService.getByCode(buyerCode)
        );
    }

    @PutMapping("/{buyerCode}")
    public ResponseEntity<BuyerResponse> update(
            @PathVariable String buyerCode,
            @Valid @RequestBody BuyerUpdateRequest request
    ) {
        return ResponseEntity.ok(
                buyerService.update(buyerCode, request)
        );
    }

    @PatchMapping("/{buyerCode}/activate")
    public ResponseEntity<BuyerResponse> activate(
            @PathVariable String buyerCode
    ) {
        return ResponseEntity.ok(
                buyerService.activate(buyerCode)
        );
    }

    @PatchMapping("/{buyerCode}/deactivate")
    public ResponseEntity<BuyerResponse> deactivate(
            @PathVariable String buyerCode
    ) {
        return ResponseEntity.ok(
                buyerService.deactivate(buyerCode)
        );
    }

    @DeleteMapping("/{buyerCode}")
    public ResponseEntity<Void> delete(
            @PathVariable String buyerCode
    ) {
        buyerService.softDelete(buyerCode);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{buyerCode}/restore")
    public ResponseEntity<BuyerResponse> restore(
            @PathVariable String buyerCode
    ) {
        return ResponseEntity.ok(
                buyerService.restore(buyerCode)
        );
    }
}