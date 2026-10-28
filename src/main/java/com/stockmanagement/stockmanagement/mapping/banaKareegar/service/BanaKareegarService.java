package com.stockmanagement.stockmanagement.mapping.banaKareegar.service;

import com.stockmanagement.stockmanagement.mapping.banaKareegar.dto.*;

import java.util.List;

public interface BanaKareegarService {

    BanaKareegarResponse create(
            BanaKareegarCreateRequest request
    );

    BanaKareegarResponse getByCode(
            String code
    );

    List<BanaKareegarResponse> getAll();

    List<BanaKareegarResponse> getActive();

    List<BanaKareegarResponse> getDeleted();

    List<BanaKareegarResponse> getByKareegar(
            String kareegarCode
    );

    BanaKareegarResponse update(
            String code,
            BanaKareegarUpdateRequest request
    );

    BanaKareegarResponse activate(
            String code
    );

    BanaKareegarResponse deactivate(
            String code
    );

    void delete(
            String code
    );

    BanaKareegarResponse restore(
            String code
    );
}