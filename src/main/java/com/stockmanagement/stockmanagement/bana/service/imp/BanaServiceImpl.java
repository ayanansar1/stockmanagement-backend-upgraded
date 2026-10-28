package com.stockmanagement.stockmanagement.bana.service.imp;

import com.stockmanagement.stockmanagement.bana.dto.BanaCreateRequest;
import com.stockmanagement.stockmanagement.bana.dto.BanaResponse;
import com.stockmanagement.stockmanagement.bana.dto.BanaUpdateRequest;
import com.stockmanagement.stockmanagement.bana.entity.Bana;
import com.stockmanagement.stockmanagement.bana.mapper.BanaMapper;
import com.stockmanagement.stockmanagement.bana.repository.BanaRepository;
import com.stockmanagement.stockmanagement.bana.service.BanaService;
import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.exception.DuplicateResourceException;
import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BanaServiceImpl implements BanaService {

    private final BanaRepository banaRepository;
    private final BanaMapper banaMapper;
    private final BusinessCodeService businessCodeService;


    @Override
    public BanaResponse create(BanaCreateRequest request) {

        if (banaRepository
                .existsByBanaNameIgnoreCaseAndDeletedFalse(
                        request.banaName())) {

            throw new DuplicateResourceException(
                    "Bana already exists: " + request.banaName()
            );
        }

        Bana bana = banaMapper.toEntity(request);

        String banaCode =
                businessCodeService.generateCode(CodeType.BANA);

        bana.setBanaCode(banaCode);
        bana.setStatus(Status.ACTIVE);
        bana.setDeleted(false);
        bana.setDeletedAt(null);

        Bana savedBana = banaRepository.save(bana);

        return banaMapper.toResponse(savedBana);
    }


    @Override
    @Transactional(readOnly = true)
    public BanaResponse getByCode(String banaCode) {

        Bana bana = findNotDeleted(banaCode);

        return banaMapper.toResponse(bana);
    }


    @Override
    @Transactional(readOnly = true)
    public List<BanaResponse> getAll() {

        return banaRepository.findAllByDeletedFalse()
                .stream()
                .map(banaMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<BanaResponse> getActive() {

        return banaRepository
                .findAllByStatusAndDeletedFalse(Status.ACTIVE)
                .stream()
                .map(banaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BanaResponse> getInactive() {

        return banaRepository
                .findAllByStatusAndDeletedFalse(Status.INACTIVE)
                .stream()
                .map(banaMapper::toResponse)
                .toList();
    }


    @Override
    public BanaResponse update(
            String banaCode,
            BanaUpdateRequest request) {

        Bana bana = findNotDeleted(banaCode);

        if (banaRepository
                .existsByBanaNameIgnoreCaseAndIdNotAndDeletedFalse(
                        request.banaName(),
                        bana.getId())) {

            throw new DuplicateResourceException(
                    "Bana already exists: " + request.banaName()
            );
        }

        banaMapper.updateEntity(request, bana);

        return banaMapper.toResponse(bana);
    }


    @Override
    public BanaResponse activate(String banaCode) {

        Bana bana = findNotDeleted(banaCode);

        bana.setStatus(Status.ACTIVE);

        return banaMapper.toResponse(bana);
    }


    @Override
    public BanaResponse deactivate(String banaCode) {

        Bana bana = findNotDeleted(banaCode);

        bana.setStatus(Status.INACTIVE);

        return banaMapper.toResponse(bana);
    }


    @Override
    public void softDelete(String banaCode) {

        Bana bana = findNotDeleted(banaCode);

        bana.setStatus(Status.INACTIVE);
        bana.softDelete();
    }


    @Override
    public BanaResponse restore(String banaCode) {

        Bana bana = banaRepository
                .findByBanaCodeAndDeletedTrue(banaCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Deleted Bana not found: " + banaCode
                        )
                );

        bana.restore();
        bana.setStatus(Status.ACTIVE);

        return banaMapper.toResponse(bana);
    }


    @Override
    @Transactional(readOnly = true)
    public List<BanaResponse> getDeleted() {

        return banaRepository.findAllByDeletedTrue()
                .stream()
                .map(banaMapper::toResponse)
                .toList();
    }


    private Bana findNotDeleted(String banaCode) {

        return banaRepository
                .findByBanaCodeAndDeletedFalse(banaCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bana not found: " + banaCode
                        )
                );
    }
}