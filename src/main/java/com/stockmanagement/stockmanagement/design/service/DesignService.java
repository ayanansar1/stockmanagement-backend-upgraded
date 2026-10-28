package com.stockmanagement.stockmanagement.design.service;

import com.stockmanagement.stockmanagement.design.dto.DesignCreateRequest;
import com.stockmanagement.stockmanagement.design.dto.DesignResponse;
import com.stockmanagement.stockmanagement.design.dto.DesignUpdateRequest;

import java.util.List;

public interface DesignService {

    DesignResponse create(DesignCreateRequest request);

    DesignResponse getByCode(String designCode);

    List<DesignResponse> getAll();

    List<DesignResponse> getActive();

    List<DesignResponse> getInactive();


    DesignResponse update(
            String designCode,
            DesignUpdateRequest request
    );

    DesignResponse activate(String designCode);

    DesignResponse deactivate(String designCode);

    void softDelete(String designCode);

    DesignResponse restore(String designCode);

    List<DesignResponse> getDeleted();
}