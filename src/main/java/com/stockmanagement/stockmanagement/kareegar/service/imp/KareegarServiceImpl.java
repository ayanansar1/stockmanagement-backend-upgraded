package com.stockmanagement.stockmanagement.kareegar.service.imp;


import com.stockmanagement.stockmanagement.common.enums.Role;
import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.exception.DuplicateResourceException;
import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
import com.stockmanagement.stockmanagement.kareegar.dto.*;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.kareegar.mapper.KareegarMapper;
import com.stockmanagement.stockmanagement.kareegar.repository.KareegarRepository;
import com.stockmanagement.stockmanagement.kareegar.service.KareegarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class KareegarServiceImpl
        implements KareegarService {

    private final KareegarRepository repository;

    private final KareegarMapper mapper;

    private final BusinessCodeService codeService;


    @Override
    public KareegarResponse create(
            KareegarCreateRequest request) {

        validateMobileForCreate(
                request.mobileNo()
        );

        validateEmailForCreate(
                request.emailId()
        );

        Kareegar kareegar =
                mapper.toEntity(request);

        String code =
                codeService.generateCode(
                        CodeType.KAREEGAR
                );

        kareegar.setKareegarCode(code);

        kareegar.setRole(
                Role.KAREEGAR
        );

        kareegar.setStatus(
                Status.ACTIVE
        );

        kareegar.setDeleted(false);

        Kareegar saved =
                repository.save(kareegar);

        return mapper.toResponse(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public KareegarResponse getByCode(
            String code) {

        Kareegar kareegar =
                findActiveByCode(code);

        return mapper.toResponse(kareegar);
    }


    @Override
    @Transactional(readOnly = true)
    public List<KareegarResponse> getAll() {

        return repository
                .findAllByDeletedFalse()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    @Override
    public KareegarResponse update(
            String code,
            KareegarUpdateRequest request) {

        Kareegar kareegar =
                findActiveByCode(code);

        validateMobileForUpdate(
                request.mobileNo(),
                kareegar.getId()
        );

        validateEmailForUpdate(
                request.emailId(),
                kareegar.getId()
        );

        mapper.updateEntity(
                request,
                kareegar
        );

        return mapper.toResponse(kareegar);
    }


    @Override
    public KareegarResponse activate(
            String code) {

        Kareegar kareegar =
                findActiveByCode(code);

        if (kareegar.getStatus()
                == Status.ACTIVE) {

            return mapper.toResponse(kareegar);
        }

        kareegar.setStatus(
                Status.ACTIVE
        );

        return mapper.toResponse(kareegar);
    }


    @Override
    public KareegarResponse deactivate(
            String code) {

        Kareegar kareegar =
                findActiveByCode(code);

        if (kareegar.getStatus()
                == Status.INACTIVE) {

            return mapper.toResponse(kareegar);
        }

        kareegar.setStatus(
                Status.INACTIVE
        );

        return mapper.toResponse(kareegar);
    }


    @Override
    public void softDelete(
            String code) {

        Kareegar kareegar =
                findActiveByCode(code);

        kareegar.softDelete();

        kareegar.setStatus(
                Status.INACTIVE
        );
    }


    @Override
    public KareegarResponse restore(
            String code) {

        Kareegar kareegar =
                repository
                        .findByKareegarCodeAndDeletedTrue(code)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Deleted Kareegar not found: "
                                                + code
                                )
                        );

        kareegar.restore();

        kareegar.setStatus(
                Status.ACTIVE
        );

        return mapper.toResponse(kareegar);
    }


    @Override
    @Transactional(readOnly = true)
    public List<KareegarResponse> getDeleted() {

        return repository
                .findAllByDeletedTrue()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    private Kareegar findActiveByCode(
            String code) {

        return repository
                .findByKareegarCodeAndDeletedFalse(code)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Kareegar not found: " + code
                        )
                );
    }


    private void validateMobileForCreate(
            String mobileNo) {

        if (repository.existsByMobileNo(
                mobileNo)) {

            throw new DuplicateResourceException(
                    "Mobile number already exists"
            );
        }
    }


    private void validateEmailForCreate(
            String emailId) {

        if (emailId == null ||
                emailId.isBlank()) {

            return;
        }

        if (repository.existsByEmailId(
                emailId)) {

            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }
    }


    private void validateMobileForUpdate(
            String mobileNo,
            Long id) {

        if (repository.existsByMobileNoAndIdNot(
                mobileNo,
                id)) {

            throw new DuplicateResourceException(
                    "Mobile number already exists"
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<KareegarResponse> getAllActive() {

        return repository
                .findAllByDeletedFalseAndStatus(Status.ACTIVE)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KareegarResponse> getAllInactive() {

        return repository
                .findAllByDeletedFalseAndStatus(Status.INACTIVE)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    private void validateEmailForUpdate(
            String emailId,
            Long id) {

        if (emailId == null ||
                emailId.isBlank()) {

            return;
        }

        if (repository.existsByEmailIdAndIdNot(
                emailId,
                id)) {

            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }
    }
}
