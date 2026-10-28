package com.stockmanagement.stockmanagement.mapping.banaKareegar.mapper;

import com.stockmanagement.stockmanagement.mapping.banaKareegar.dto.BanaKareegarResponse;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.entity.BanaKareegar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BanaKareegarMapper {

    @Mapping(
            target = "kareegarCode",
            source = "kareegar.kareegarCode"
    )
    @Mapping(
            target = "kareegarName",
            source = "kareegar.kareegarName"
    )
    @Mapping(
            target = "banaCode",
            source = "bana.banaCode"
    )
    @Mapping(
            target = "banaName",
            source = "bana.banaName"
    )
    @Mapping(
            target = "machineCode",
            source = "machine.machineCode"
    )
    BanaKareegarResponse toResponse(BanaKareegar entity);
}