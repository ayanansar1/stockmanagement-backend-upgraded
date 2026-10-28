package com.stockmanagement.stockmanagement.mapping.banaKareegar.controller;

import com.stockmanagement.stockmanagement.mapping.banaKareegar.dto.*;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.service.BanaKareegarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bana-kareegars")
@RequiredArgsConstructor
public class BanaKareegarController {

    private final BanaKareegarService service;


    @PostMapping
    public ResponseEntity<BanaKareegarResponse> create(
            @Valid @RequestBody BanaKareegarCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }


    @GetMapping
    public ResponseEntity<List<BanaKareegarResponse>> getAll() {

        return ResponseEntity.ok(
                service.getAll()
        );
    }


    @GetMapping("/active")
    public ResponseEntity<List<BanaKareegarResponse>> getActive() {

        return ResponseEntity.ok(
                service.getActive()
        );
    }


    @GetMapping("/deleted")
    public ResponseEntity<List<BanaKareegarResponse>> getDeleted() {

        return ResponseEntity.ok(
                service.getDeleted()
        );
    }


    @GetMapping("/kareegar/{kareegarCode}")
    public ResponseEntity<List<BanaKareegarResponse>>
    getByKareegar(
            @PathVariable String kareegarCode) {

        return ResponseEntity.ok(
                service.getByKareegar(kareegarCode)
        );
    }


    @GetMapping("/{banaKareegarCode}")
    public ResponseEntity<BanaKareegarResponse>
    getByCode(
            @PathVariable String banaKareegarCode) {

        return ResponseEntity.ok(
                service.getByCode(banaKareegarCode)
        );
    }


    @PutMapping("/{banaKareegarCode}")
    public ResponseEntity<BanaKareegarResponse>
    update(
            @PathVariable String banaKareegarCode,
            @Valid @RequestBody BanaKareegarUpdateRequest request) {

        return ResponseEntity.ok(
                service.update(
                        banaKareegarCode,
                        request
                )
        );
    }


    @PatchMapping("/{banaKareegarCode}/activate")
    public ResponseEntity<BanaKareegarResponse>
    activate(
            @PathVariable String banaKareegarCode) {

        return ResponseEntity.ok(
                service.activate(banaKareegarCode)
        );
    }


    @PatchMapping("/{banaKareegarCode}/deactivate")
    public ResponseEntity<BanaKareegarResponse>
    deactivate(
            @PathVariable String banaKareegarCode) {

        return ResponseEntity.ok(
                service.deactivate(banaKareegarCode)
        );
    }


    @DeleteMapping("/{banaKareegarCode}")
    public ResponseEntity<Void> delete(
            @PathVariable String banaKareegarCode) {

        service.delete(banaKareegarCode);

        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{banaKareegarCode}/restore")
    public ResponseEntity<BanaKareegarResponse>
    restore(
            @PathVariable String banaKareegarCode) {

        return ResponseEntity.ok(
                service.restore(banaKareegarCode)
        );
    }
}