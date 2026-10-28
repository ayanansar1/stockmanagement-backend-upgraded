package com.stockmanagement.stockmanagement.machine.mapper;



import com.stockmanagement.stockmanagement.machine.dto.MachineCreateRequest;
import com.stockmanagement.stockmanagement.machine.dto.MachineResponse;
import com.stockmanagement.stockmanagement.machine.dto.MachineUpdateRequest;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface MachineMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "machineCode", ignore = true)
    @Mapping(target = "machineNo", ignore = true)
    @Mapping(target = "kareegar", ignore = true)
    @Mapping(target = "machineStatus", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Machine toEntity(MachineCreateRequest request);


    @Mapping(
            target = "kareegarCode",
            source = "kareegar.kareegarCode"
    )
    MachineResponse toResponse(Machine machine);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "machineCode", ignore = true)
    @Mapping(target = "machineNo", ignore = true)
    @Mapping(target = "kareegar", ignore = true)
    @Mapping(target = "machineStatus", ignore = true)
    @Mapping(target = "dateOfJoin", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntity(
            MachineUpdateRequest request,
            @MappingTarget Machine machine
    );
}
