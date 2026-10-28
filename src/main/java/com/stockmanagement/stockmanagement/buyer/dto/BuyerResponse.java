package com.stockmanagement.stockmanagement.buyer.dto;

import com.stockmanagement.stockmanagement.common.enums.Status;

import java.time.LocalDateTime;

public record BuyerResponse(

        String buyerCode,

        String buyerName,

        String firmName,

        String mobileNo,

        String emailId,

        String address,

        Status status,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        Long version,

        boolean deleted,

        LocalDateTime deletedAt
) {
}