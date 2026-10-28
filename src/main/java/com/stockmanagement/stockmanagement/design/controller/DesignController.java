package com.stockmanagement.stockmanagement.design.controller;

import com.stockmanagement.stockmanagement.design.dto.DesignCreateRequest;
import com.stockmanagement.stockmanagement.design.dto.DesignResponse;
import com.stockmanagement.stockmanagement.design.dto.DesignUpdateRequest;
import com.stockmanagement.stockmanagement.design.service.DesignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/designs")
@RequiredArgsConstructor
public class DesignController {

    private final DesignService designService;


    @PostMapping
    public ResponseEntity<DesignResponse> create(
            @Valid @RequestBody
            DesignCreateRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        designService.create(request)
                );
    }


    @GetMapping
    public List<DesignResponse> getAll() {

        return designService.getAll();
    }


    @GetMapping("/active")
    public List<DesignResponse> getActive() {

        return designService.getActive();
    }

    @GetMapping("/inactive")
    public List<DesignResponse> getInactive() {

        return designService.getInactive();
    }


    @GetMapping("/deleted")
    public List<DesignResponse> getDeleted() {

        return designService.getDeleted();
    }


    @GetMapping("/{designCode}")
    public DesignResponse getByCode(
            @PathVariable String designCode) {

        return designService.getByCode(
                designCode
        );
    }


    @PutMapping("/{designCode}")
    public DesignResponse update(
            @PathVariable String designCode,
            @Valid @RequestBody
            DesignUpdateRequest request) {

        return designService.update(
                designCode,
                request
        );
    }


    @PatchMapping("/{designCode}/activate")
    public DesignResponse activate(
            @PathVariable String designCode) {

        return designService.activate(
                designCode
        );
    }


    @PatchMapping("/{designCode}/deactivate")
    public DesignResponse deactivate(
            @PathVariable String designCode) {

        return designService.deactivate(
                designCode
        );
    }


    @DeleteMapping("/{designCode}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable String designCode) {

        designService.softDelete(
                designCode
        );
    }


    @PatchMapping("/{designCode}/restore")
    public DesignResponse restore(
            @PathVariable String designCode) {

        return designService.restore(
                designCode
        );
    }
}