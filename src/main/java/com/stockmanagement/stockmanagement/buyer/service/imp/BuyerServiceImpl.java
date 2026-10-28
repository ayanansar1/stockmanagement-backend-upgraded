package com.stockmanagement.stockmanagement.buyer.service.imp;


import com.stockmanagement.stockmanagement.buyer.dto.BuyerCreateRequest;
import com.stockmanagement.stockmanagement.buyer.dto.BuyerResponse;
import com.stockmanagement.stockmanagement.buyer.dto.BuyerUpdateRequest;
import com.stockmanagement.stockmanagement.buyer.entity.Buyer;
import com.stockmanagement.stockmanagement.buyer.mapper.BuyerMapper;
import com.stockmanagement.stockmanagement.buyer.repository.BuyerRepository;
import com.stockmanagement.stockmanagement.buyer.service.BuyerService;
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
public class BuyerServiceImpl implements BuyerService {

    private final BuyerRepository buyerRepository;
    private final BuyerMapper buyerMapper;
    private final BusinessCodeService businessCodeService;

    @Override
    @Transactional
    public BuyerResponse create(BuyerCreateRequest request) {

        if (buyerRepository.existsByMobileNoAndDeletedFalse(
                request.mobileNo()
        )) {
            throw new DuplicateResourceException(
                    "Buyer already exists with mobile number: "
                            + request.mobileNo()
            );
        }

        Buyer buyer = Buyer.builder()
                .buyerCode(
                        businessCodeService.generateCode(
                                CodeType.BUYER
                        )
                )
                .buyerName(request.buyerName())
                .firmName(request.firmName())
                .mobileNo(request.mobileNo())
                .emailId(request.emailId())
                .address(request.address())
                .status(Status.ACTIVE)
                .build();

        Buyer savedBuyer = buyerRepository.save(buyer);

        return buyerMapper.toResponse(savedBuyer);
    }

    @Override
    @Transactional(readOnly = true)
    public BuyerResponse getByCode(String buyerCode) {

        Buyer buyer = buyerRepository
                .findByBuyerCodeAndDeletedFalse(buyerCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Buyer not found with code: "
                                        + buyerCode
                        )
                );

        return buyerMapper.toResponse(buyer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuyerResponse> getAll() {

        return buyerRepository
                .findAllByDeletedFalse()
                .stream()
                .map(buyerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuyerResponse> getActive() {

        return buyerRepository
                .findAllByStatusAndDeletedFalse(Status.ACTIVE)
                .stream()
                .map(buyerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuyerResponse> getInactive() {

        return buyerRepository
                .findAllByStatusAndDeletedFalse(Status.INACTIVE)
                .stream()
                .map(buyerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BuyerResponse> getDeleted() {

        return buyerRepository
                .findAllByDeletedTrue()
                .stream()
                .map(buyerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public BuyerResponse update(
            String buyerCode,
            BuyerUpdateRequest request
    ) {

        Buyer buyer = buyerRepository
                .findByCodeForUpdate(buyerCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Buyer not found with code: "
                                        + buyerCode
                        )
                );

        if (buyerRepository
                .existsByMobileNoAndDeletedFalseAndIdNot(
                        request.mobileNo(),
                        buyer.getId()
                )) {

            throw new DuplicateResourceException(
                    "Mobile number already exists: "
                            + request.mobileNo()
            );
        }

        buyer.setBuyerName(request.buyerName());
        buyer.setFirmName(request.firmName());
        buyer.setMobileNo(request.mobileNo());
        buyer.setEmailId(request.emailId());
        buyer.setAddress(request.address());

        return buyerMapper.toResponse(
                buyerRepository.save(buyer)
        );
    }

    @Override
    @Transactional
    public BuyerResponse activate(String buyerCode) {

        Buyer buyer = buyerRepository
                .findByCodeForUpdate(buyerCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Buyer not found with code: "
                                        + buyerCode
                        )
                );

        buyer.setStatus(Status.ACTIVE);

        return buyerMapper.toResponse(
                buyerRepository.save(buyer)
        );
    }

    @Override
    @Transactional
    public BuyerResponse deactivate(String buyerCode) {

        Buyer buyer = buyerRepository
                .findByCodeForUpdate(buyerCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Buyer not found with code: "
                                        + buyerCode
                        )
                );

        buyer.setStatus(Status.INACTIVE);

        return buyerMapper.toResponse(
                buyerRepository.save(buyer)
        );
    }

    @Override
    @Transactional
    public void softDelete(String buyerCode) {

        Buyer buyer = buyerRepository
                .findByCodeForUpdate(buyerCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Buyer not found with code: "
                                        + buyerCode
                        )
                );

        buyer.setStatus(Status.INACTIVE);
        buyer.softDelete();

        buyerRepository.save(buyer);
    }

    @Override
    @Transactional
    public BuyerResponse restore(String buyerCode) {

        Buyer buyer = buyerRepository
                .findByBuyerCodeAndDeletedTrue(buyerCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Deleted buyer not found with code: "
                                        + buyerCode
                        )
                );

        if (buyerRepository.existsByMobileNoAndDeletedFalse(
                buyer.getMobileNo()
        )) {
            throw new DuplicateResourceException(
                    "Another active buyer already uses mobile number: "
                            + buyer.getMobileNo()
            );
        }

        buyer.restore();
        buyer.setStatus(Status.ACTIVE);

        return buyerMapper.toResponse(
                buyerRepository.save(buyer)
        );
    }
}