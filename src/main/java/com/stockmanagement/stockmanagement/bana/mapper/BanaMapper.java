package com.stockmanagement.stockmanagement.bana.mapper;


import com.stockmanagement.stockmanagement.bana.dto.BanaCreateRequest;
import com.stockmanagement.stockmanagement.bana.dto.BanaResponse;
import com.stockmanagement.stockmanagement.bana.dto.BanaUpdateRequest;
import com.stockmanagement.stockmanagement.bana.entity.Bana;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BanaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "banaCode", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Bana toEntity(BanaCreateRequest request);


    BanaResponse toResponse(Bana bana);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "banaCode", ignore = true)
    @Mapping(target = "dateOfJoin", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntity(
            BanaUpdateRequest request,
            @MappingTarget Bana bana
    );
}