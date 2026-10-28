package com.stockmanagement.stockmanagement.design.mapper;



import com.stockmanagement.stockmanagement.design.dto.DesignCreateRequest;
import com.stockmanagement.stockmanagement.design.dto.DesignResponse;
import com.stockmanagement.stockmanagement.design.dto.DesignUpdateRequest;
import com.stockmanagement.stockmanagement.design.entity.Design;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DesignMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "designCode", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Design toEntity(DesignCreateRequest request);


    DesignResponse toResponse(Design design);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "designCode", ignore = true)
    @Mapping(target = "dateOfJoin", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntity(
            DesignUpdateRequest request,
            @MappingTarget Design design
    );
}
