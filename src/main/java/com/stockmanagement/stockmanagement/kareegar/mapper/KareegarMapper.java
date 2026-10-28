package com.stockmanagement.stockmanagement.kareegar.mapper;

import com.stockmanagement.stockmanagement.kareegar.dto.KareegarCreateRequest;
import com.stockmanagement.stockmanagement.kareegar.dto.KareegarResponse;
import com.stockmanagement.stockmanagement.kareegar.dto.KareegarUpdateRequest;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface KareegarMapper {

    // =========================
    // Create DTO -> Entity
    // =========================
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kareegarCode", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    Kareegar toEntity(KareegarCreateRequest request);


    // =========================
    // Entity -> Response DTO
    // ===========a==============
    KareegarResponse toResponse(Kareegar entity);


    // =========================
    // Update DTO -> Existing Entity
    // =========================
    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "kareegarCode", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "joinDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    void updateEntity(
            KareegarUpdateRequest request,
            @MappingTarget Kareegar entity
    );
}