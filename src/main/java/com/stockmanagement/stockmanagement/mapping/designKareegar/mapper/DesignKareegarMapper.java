package com.stockmanagement.stockmanagement.mapping.designKareegar.mapper;

import com.stockmanagement.stockmanagement.mapping.designKareegar.dto.DesignKareegarResponse;
import com.stockmanagement.stockmanagement.mapping.designKareegar.entity.DesignKareegar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DesignKareegarMapper {

    @Mapping(
            target = "kareegarCode",
            source = "kareegar.kareegarCode"
    )
    @Mapping(
            target = "kareegarName",
            source = "kareegar.kareegarName"
    )
    @Mapping(
            target = "designCode",
            source = "design.designCode"
    )
    @Mapping(
            target = "designName",
            source = "design.designName"
    )
    @Mapping(
            target = "machineCode",
            source = "machine.machineCode"
    )
    DesignKareegarResponse toResponse(
            DesignKareegar entity
    );
}