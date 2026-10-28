package com.stockmanagement.stockmanagement.machine.dto;


import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.machine.enums.JacquardType;
import com.stockmanagement.stockmanagement.machine.enums.MachineType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record MachineResponse(

        String machineCode,

        Long machineNo,

        String kareegarCode,

        MachineType machineType,

        JacquardType jacquardType,

        Integer jacquardChok,

        LocalDate dateOfJoin,

        Status machineStatus,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        Long version,

        boolean deleted,

        LocalDateTime deletedAt

) {
}
