package com.stockmanagement.stockmanagement.kareegar.service;


import com.stockmanagement.stockmanagement.kareegar.dto.*;

import java.util.List;

public interface KareegarService {

    KareegarResponse create(
            KareegarCreateRequest request
    );

    KareegarResponse getByCode(
            String code
    );

    List<KareegarResponse> getAll();

    KareegarResponse update(
            String code,
            KareegarUpdateRequest request
    );

    KareegarResponse activate(
            String code
    );

    KareegarResponse deactivate(
            String code
    );

    void softDelete(
            String code
    );

    KareegarResponse restore(
            String code
    );

    List<KareegarResponse> getDeleted();
    List<KareegarResponse> getAllActive();

    List<KareegarResponse> getAllInactive();
}