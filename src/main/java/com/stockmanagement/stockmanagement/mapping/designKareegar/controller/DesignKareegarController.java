package com.stockmanagement.stockmanagement.mapping.designKareegar.controller;

import com.stockmanagement.stockmanagement.mapping.designKareegar.dto.*;
import com.stockmanagement.stockmanagement.mapping.designKareegar.service.DesignKareegarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/design-kareegars")
@RequiredArgsConstructor
public class DesignKareegarController {

    private final DesignKareegarService service;


    @PostMapping
    public ResponseEntity<DesignKareegarResponse> create(
            @Valid @RequestBody
            DesignKareegarCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.create(request));
    }


    @GetMapping
    public ResponseEntity<List<DesignKareegarResponse>>
    getAll() {

        return ResponseEntity.ok(
                service.getAll()
        );
    }


    @GetMapping("/active")
    public ResponseEntity<List<DesignKareegarResponse>>
    getActive() {

        return ResponseEntity.ok(
                service.getActive()
        );
    }


    @GetMapping("/deleted")
    public ResponseEntity<List<DesignKareegarResponse>>
    getDeleted() {

        return ResponseEntity.ok(
                service.getDeleted()
        );
    }


    @GetMapping("/kareegar/{kareegarCode}")
    public ResponseEntity<List<DesignKareegarResponse>>
    getByKareegar(
            @PathVariable String kareegarCode) {

        return ResponseEntity.ok(
                service.getByKareegar(kareegarCode)
        );
    }


    @GetMapping("/{designKareegarCode}")
    public ResponseEntity<DesignKareegarResponse>
    getByCode(
            @PathVariable String designKareegarCode) {

        return ResponseEntity.ok(
                service.getByCode(designKareegarCode)
        );
    }


    @PutMapping("/{designKareegarCode}")
    public ResponseEntity<DesignKareegarResponse>
    update(
            @PathVariable String designKareegarCode,
            @Valid @RequestBody
            DesignKareegarUpdateRequest request) {

        return ResponseEntity.ok(
                service.update(
                        designKareegarCode,
                        request
                )
        );
    }


    @PatchMapping("/{designKareegarCode}/activate")
    public ResponseEntity<DesignKareegarResponse>
    activate(
            @PathVariable String designKareegarCode) {

        return ResponseEntity.ok(
                service.activate(designKareegarCode)
        );
    }


    @PatchMapping("/{designKareegarCode}/deactivate")
    public ResponseEntity<DesignKareegarResponse>
    deactivate(
            @PathVariable String designKareegarCode) {

        return ResponseEntity.ok(
                service.deactivate(designKareegarCode)
        );
    }


    @DeleteMapping("/{designKareegarCode}")
    public ResponseEntity<Void> delete(
            @PathVariable String designKareegarCode) {

        service.delete(designKareegarCode);

        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{designKareegarCode}/restore")
    public ResponseEntity<DesignKareegarResponse>
    restore(
            @PathVariable String designKareegarCode) {

        return ResponseEntity.ok(
                service.restore(designKareegarCode)
        );
    }
}