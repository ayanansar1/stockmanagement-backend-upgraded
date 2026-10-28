package com.stockmanagement.stockmanagement.mapping.designKareegar.service;

import com.stockmanagement.stockmanagement.mapping.designKareegar.dto.*;

import java.util.List;

public interface DesignKareegarService {

    DesignKareegarResponse create(
            DesignKareegarCreateRequest request
    );

    DesignKareegarResponse getByCode(
            String code
    );

    List<DesignKareegarResponse> getAll();

    List<DesignKareegarResponse> getActive();

    List<DesignKareegarResponse> getDeleted();

    List<DesignKareegarResponse> getByKareegar(
            String kareegarCode
    );

    DesignKareegarResponse update(
            String code,
            DesignKareegarUpdateRequest request
    );

    DesignKareegarResponse activate(
            String code
    );

    DesignKareegarResponse deactivate(
            String code
    );

    void delete(
            String code
    );

    DesignKareegarResponse restore(
            String code
    );
}