package com.stockmanagement.stockmanagement.kareegar.controller;



import com.stockmanagement.stockmanagement.kareegar.dto.*;
import com.stockmanagement.stockmanagement.kareegar.service.KareegarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kareegars")
@RequiredArgsConstructor
public class KareegarController {

    private final KareegarService service;


    @PostMapping
    public ResponseEntity<KareegarResponse> create(
            @Valid
            @RequestBody
            KareegarCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }


    @GetMapping
    public ResponseEntity<List<KareegarResponse>> getAll() {

        return ResponseEntity.ok(
                service.getAll()
        );
    }


    @GetMapping("/code/{code}")
    public ResponseEntity<KareegarResponse> getByCode(
            @PathVariable String code) {

        return ResponseEntity.ok(
                service.getByCode(code)
        );
    }


    @PutMapping("/code/{code}")
    public ResponseEntity<KareegarResponse> update(
            @PathVariable String code,

            @Valid
            @RequestBody
            KareegarUpdateRequest request) {

        return ResponseEntity.ok(
                service.update(code, request)
        );
    }


    @PatchMapping(
            "/code/{code}/activate"
    )
    public ResponseEntity<KareegarResponse> activate(
            @PathVariable String code) {

        return ResponseEntity.ok(
                service.activate(code)
        );
    }


    @PatchMapping(
            "/code/{code}/deactivate"
    )
    public ResponseEntity<KareegarResponse> deactivate(
            @PathVariable String code) {

        return ResponseEntity.ok(
                service.deactivate(code)
        );
    }


    @DeleteMapping("/code/{code}")
    public ResponseEntity<Void> delete(
            @PathVariable String code) {

        service.softDelete(code);

        return ResponseEntity.noContent()
                .build();
    }


    @PatchMapping(
            "/code/{code}/restore"
    )
    public ResponseEntity<KareegarResponse> restore(
            @PathVariable String code) {

        return ResponseEntity.ok(
                service.restore(code)
        );
    }

    @GetMapping("/active")
    public ResponseEntity<List<KareegarResponse>> getAllActive() {

        return ResponseEntity.ok(
                service.getAllActive()
        );
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<KareegarResponse>> getAllInactive() {

        return ResponseEntity.ok(
                service.getAllInactive()
        );
    }


    @GetMapping("/deleted")
    public ResponseEntity<List<KareegarResponse>>
    getDeleted() {

        return ResponseEntity.ok(
                service.getDeleted()
        );
    }
}