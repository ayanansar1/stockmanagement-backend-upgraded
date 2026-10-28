package com.stockmanagement.stockmanagement.salesinvoice.controller;

import com.stockmanagement.stockmanagement.salesinvoice.dto.*;
import com.stockmanagement.stockmanagement.salesinvoice.service.SalesInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class SalesInvoiceController {

    private final SalesInvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<SalesInvoiceResponse> createDraft(
            @Valid @RequestBody CreateInvoiceRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        invoiceService.createDraft(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<SalesInvoiceResponse>> getAll() {

        return ResponseEntity.ok(
                invoiceService.getAll()
        );
    }

    @GetMapping("/{invoiceCode}")
    public ResponseEntity<SalesInvoiceResponse> getByCode(
            @PathVariable String invoiceCode
    ) {

        return ResponseEntity.ok(
                invoiceService.getByCode(invoiceCode)
        );
    }

    @PostMapping("/{invoiceCode}/items")
    public ResponseEntity<SalesInvoiceResponse> addItem(
            @PathVariable String invoiceCode,
            @Valid @RequestBody AddInvoiceItemRequest request
    ) {

        return ResponseEntity.ok(
                invoiceService.addItem(
                        invoiceCode,
                        request
                )
        );
    }

    @PatchMapping("/{invoiceCode}/finalize")
    public ResponseEntity<SalesInvoiceResponse> finalizeInvoice(
            @PathVariable String invoiceCode
    ) {

        return ResponseEntity.ok(
                invoiceService.finalizeInvoice(
                        invoiceCode
                )
        );
    }
}