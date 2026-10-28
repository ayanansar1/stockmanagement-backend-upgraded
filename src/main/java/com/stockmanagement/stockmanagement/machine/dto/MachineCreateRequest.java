package com.stockmanagement.stockmanagement.machine.dto;



import com.stockmanagement.stockmanagement.machine.enums.JacquardType;
import com.stockmanagement.stockmanagement.machine.enums.MachineType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MachineCreateRequest(

        @NotNull(message = "Machine type is required")
        MachineType machineType,

        @NotNull(message = "Jacquard type is required")
        JacquardType jacquardType,

        @NotNull(message = "Jacquard chok is required")
        @Min(
                value = 1,
                message = "Jacquard chok must be at least 1"
        )
        Integer jacquardChok,

        @NotNull(message = "Date of join is required")
        LocalDate dateOfJoin

) {
}
