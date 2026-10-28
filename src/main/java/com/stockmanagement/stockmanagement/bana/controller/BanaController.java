package com.stockmanagement.stockmanagement.bana.controller;


import com.stockmanagement.stockmanagement.bana.dto.BanaCreateRequest;
import com.stockmanagement.stockmanagement.bana.dto.BanaResponse;
import com.stockmanagement.stockmanagement.bana.dto.BanaUpdateRequest;
import com.stockmanagement.stockmanagement.bana.service.BanaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banas")
@RequiredArgsConstructor
public class BanaController {

    private final BanaService banaService;


    @PostMapping
    public ResponseEntity<BanaResponse> create(
            @Valid @RequestBody BanaCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(banaService.create(request));
    }


    @GetMapping
    public List<BanaResponse> getAll() {

        return banaService.getAll();
    }


    @GetMapping("/active")
    public List<BanaResponse> getActive() {

        return banaService.getActive();
    }

    @GetMapping("/inactive")
    public List<BanaResponse> getInactive() {

        return banaService.getInactive();
    }



    @GetMapping("/deleted")
    public List<BanaResponse> getDeleted() {

        return banaService.getDeleted();
    }


    @GetMapping("/{banaCode}")
    public BanaResponse getByCode(
            @PathVariable String banaCode) {

        return banaService.getByCode(banaCode);
    }


    @PutMapping("/{banaCode}")
    public BanaResponse update(
            @PathVariable String banaCode,
            @Valid @RequestBody BanaUpdateRequest request) {

        return banaService.update(banaCode, request);
    }


    @PatchMapping("/{banaCode}/activate")
    public BanaResponse activate(
            @PathVariable String banaCode) {

        return banaService.activate(banaCode);
    }


    @PatchMapping("/{banaCode}/deactivate")
    public BanaResponse deactivate(
            @PathVariable String banaCode) {

        return banaService.deactivate(banaCode);
    }


    @DeleteMapping("/{banaCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable String banaCode) {

        banaService.softDelete(banaCode);
    }


    @PatchMapping("/{banaCode}/restore")
    public BanaResponse restore(
            @PathVariable String banaCode) {

        return banaService.restore(banaCode);
    }
}
