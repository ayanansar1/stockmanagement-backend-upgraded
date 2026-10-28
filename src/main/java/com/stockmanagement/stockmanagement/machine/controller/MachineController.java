package com.stockmanagement.stockmanagement.machine.controller;


import com.stockmanagement.stockmanagement.machine.dto.MachineCreateRequest;
import com.stockmanagement.stockmanagement.machine.dto.MachineResponse;
import com.stockmanagement.stockmanagement.machine.dto.MachineUpdateRequest;
import com.stockmanagement.stockmanagement.machine.service.MachineService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MachineController {

    private final MachineService machineService;


    @PostMapping(
            "/kareegars/{kareegarCode}/machines"
    )
    public ResponseEntity<MachineResponse> create(

            @PathVariable String kareegarCode,

            @Valid
            @RequestBody
            MachineCreateRequest request

    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        machineService.create(
                                kareegarCode,
                                request
                        )
                );
    }


    @GetMapping(
            "/machines/{machineCode}"
    )
    public MachineResponse getByCode(
            @PathVariable String machineCode
    ) {

        return machineService
                .getByCode(machineCode);
    }


    @GetMapping(
            "/kareegars/{kareegarCode}/machines"
    )
    public List<MachineResponse> getByKareegar(

            @PathVariable String kareegarCode

    ) {

        return machineService
                .getByKareegar(kareegarCode);
    }


    @GetMapping("/machines")
    public List<MachineResponse> getAll() {

        return machineService.getAll();
    }

    @GetMapping("/machines/active")
    public List<MachineResponse> getActive() {

        return machineService.getActive();
    }
    @GetMapping("/machines/inactive")
    public List<MachineResponse> getInactive() {

        return machineService.getInactive();
    }


    @PutMapping(
            "/machines/{machineCode}"
    )
    public MachineResponse update(

            @PathVariable String machineCode,

            @Valid
            @RequestBody
            MachineUpdateRequest request

    ) {

        return machineService.update(
                machineCode,
                request
        );
    }


    @PatchMapping(
            "/machines/{machineCode}/activate"
    )
    public MachineResponse activate(
            @PathVariable String machineCode
    ) {

        return machineService.activate(
                machineCode
        );
    }


    @PatchMapping(
            "/machines/{machineCode}/deactivate"
    )
    public MachineResponse deactivate(
            @PathVariable String machineCode
    ) {

        return machineService.deactivate(
                machineCode
        );
    }


    @DeleteMapping(
            "/machines/{machineCode}"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable String machineCode
    ) {

        machineService.softDelete(
                machineCode
        );
    }


    @PatchMapping(
            "/machines/{machineCode}/restore"
    )
    public MachineResponse restore(
            @PathVariable String machineCode
    ) {

        return machineService.restore(
                machineCode
        );
    }


    @GetMapping("/machines/deleted")
    public List<MachineResponse> getDeleted() {

        return machineService.getDeleted();
    }
}