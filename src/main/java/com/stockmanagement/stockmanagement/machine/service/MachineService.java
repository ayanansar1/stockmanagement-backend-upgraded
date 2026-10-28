package com.stockmanagement.stockmanagement.machine.service;



import com.stockmanagement.stockmanagement.machine.dto.MachineCreateRequest;
import com.stockmanagement.stockmanagement.machine.dto.MachineResponse;
import com.stockmanagement.stockmanagement.machine.dto.MachineUpdateRequest;

import java.util.List;

public interface MachineService {

    MachineResponse create(
            String kareegarCode,
            MachineCreateRequest request
    );

    MachineResponse getByCode(
            String machineCode
    );

    List<MachineResponse> getByKareegar(
            String kareegarCode
    );

    List<MachineResponse> getAll();

    List<MachineResponse> getActive();

    List<MachineResponse> getInactive();


    MachineResponse update(
            String machineCode,
            MachineUpdateRequest request
    );

    MachineResponse activate(
            String machineCode
    );

    MachineResponse deactivate(
            String machineCode
    );

    void softDelete(
            String machineCode
    );

    MachineResponse restore(
            String machineCode
    );

    List<MachineResponse> getDeleted();
}
