package com.stockmanagement.stockmanagement.bana.service;



import com.stockmanagement.stockmanagement.bana.dto.BanaCreateRequest;
import com.stockmanagement.stockmanagement.bana.dto.BanaResponse;
import com.stockmanagement.stockmanagement.bana.dto.BanaUpdateRequest;

import java.util.List;

public interface BanaService {

    BanaResponse create(BanaCreateRequest request);

    BanaResponse getByCode(String banaCode);

    List<BanaResponse> getAll();

    List<BanaResponse> getActive();
    List<BanaResponse> getInactive();

    BanaResponse update(
            String banaCode,
            BanaUpdateRequest request
    );

    BanaResponse activate(String banaCode);

    BanaResponse deactivate(String banaCode);

    void softDelete(String banaCode);

    BanaResponse restore(String banaCode);

    List<BanaResponse> getDeleted();
}
