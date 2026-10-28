package com.stockmanagement.stockmanagement.design.service.imp;



import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.exception.DuplicateResourceException;
import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
import com.stockmanagement.stockmanagement.design.dto.DesignCreateRequest;
import com.stockmanagement.stockmanagement.design.dto.DesignResponse;
import com.stockmanagement.stockmanagement.design.dto.DesignUpdateRequest;
import com.stockmanagement.stockmanagement.design.entity.Design;
import com.stockmanagement.stockmanagement.design.mapper.DesignMapper;
import com.stockmanagement.stockmanagement.design.repository.DesignRepository;

import com.stockmanagement.stockmanagement.design.service.DesignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DesignServiceImpl implements DesignService {

    private final DesignRepository designRepository;

    private final DesignMapper designMapper;

    private final BusinessCodeService businessCodeService;


    @Override
    public DesignResponse create(
            DesignCreateRequest request) {

        // Duplicate name check
        if (designRepository
                .existsByDesignNameIgnoreCaseAndDeletedFalse(
                        request.designName())) {

            throw new DuplicateResourceException(
                    "Design already exists: "
                            + request.designName()
            );
        }

        // DTO → Entity
        Design design =
                designMapper.toEntity(request);

        // Generate business code
        String designCode =
                businessCodeService.generateCode(
                        CodeType.DESIGN
                );

        design.setDesignCode(designCode);

        // Default status
        design.setStatus(Status.ACTIVE);

        // Default soft-delete state
        design.setDeleted(false);
        design.setDeletedAt(null);

        // Save
        Design savedDesign =
                designRepository.save(design);

        return designMapper.toResponse(savedDesign);
    }


    @Override
    @Transactional(readOnly = true)
    public DesignResponse getByCode(
            String designCode) {

        Design design =
                findNotDeleted(designCode);

        return designMapper.toResponse(design);
    }


    @Override
    @Transactional(readOnly = true)
    public List<DesignResponse> getAll() {

        return designRepository
                .findAllByDeletedFalse()
                .stream()
                .map(designMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<DesignResponse> getActive() {

        return designRepository
                .findAllByStatusAndDeletedFalse(
                        Status.ACTIVE
                )
                .stream()
                .map(designMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DesignResponse> getInactive() {

        return designRepository
                .findAllByStatusAndDeletedFalse(
                        Status.INACTIVE
                )
                .stream()
                .map(designMapper::toResponse)
                .toList();
    }


    @Override
    public DesignResponse update(
            String designCode,
            DesignUpdateRequest request) {

        Design design =
                findNotDeleted(designCode);

        // Duplicate name check excluding current record
        if (designRepository
                .existsByDesignNameIgnoreCaseAndIdNotAndDeletedFalse(
                        request.designName(),
                        design.getId()
                )) {

            throw new DuplicateResourceException(
                    "Design already exists: "
                            + request.designName()
            );
        }

        // Update allowed fields
        designMapper.updateEntity(
                request,
                design
        );

        return designMapper.toResponse(design);
    }


    @Override
    public DesignResponse activate(
            String designCode) {

        Design design =
                findNotDeleted(designCode);

        design.setStatus(Status.ACTIVE);

        return designMapper.toResponse(design);
    }


    @Override
    public DesignResponse deactivate(
            String designCode) {

        Design design =
                findNotDeleted(designCode);

        design.setStatus(Status.INACTIVE);

        return designMapper.toResponse(design);
    }


    @Override
    public void softDelete(
            String designCode) {

        Design design =
                findNotDeleted(designCode);

        // Deleted design should not remain ACTIVE
        design.setStatus(Status.INACTIVE);

        design.softDelete();
    }


    @Override
    public DesignResponse restore(
            String designCode) {

        Design design =
                designRepository
                        .findByDesignCodeAndDeletedTrue(
                                designCode
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Deleted Design not found: "
                                                + designCode
                                )
                        );

        design.restore();

        // Restored design becomes active
        design.setStatus(Status.ACTIVE);

        return designMapper.toResponse(design);
    }


    @Override
    @Transactional(readOnly = true)
    public List<DesignResponse> getDeleted() {

        return designRepository
                .findAllByDeletedTrue()
                .stream()
                .map(designMapper::toResponse)
                .toList();
    }


    private Design findNotDeleted(
            String designCode) {

        return designRepository
                .findByDesignCodeAndDeletedFalse(
                        designCode
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Design not found: "
                                        + designCode
                        )
                );
    }
}