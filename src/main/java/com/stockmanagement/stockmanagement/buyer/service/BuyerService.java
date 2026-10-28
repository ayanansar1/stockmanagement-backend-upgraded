package com.stockmanagement.stockmanagement.buyer.service;

import com.stockmanagement.stockmanagement.buyer.dto.BuyerCreateRequest;
import com.stockmanagement.stockmanagement.buyer.dto.BuyerResponse;
import com.stockmanagement.stockmanagement.buyer.dto.BuyerUpdateRequest;

import java.util.List;

public interface BuyerService {

    BuyerResponse create(BuyerCreateRequest request);

    BuyerResponse getByCode(String buyerCode);

    List<BuyerResponse> getAll();

    List<BuyerResponse> getActive();

    List<BuyerResponse> getInactive();

    List<BuyerResponse> getDeleted();

    BuyerResponse update(
            String buyerCode,
            BuyerUpdateRequest request
    );

    BuyerResponse activate(String buyerCode);

    BuyerResponse deactivate(String buyerCode);

    void softDelete(String buyerCode);

    BuyerResponse restore(String buyerCode);
}