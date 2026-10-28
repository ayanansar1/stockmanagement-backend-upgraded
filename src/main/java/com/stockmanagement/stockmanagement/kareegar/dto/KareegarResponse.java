package com.stockmanagement.stockmanagement.kareegar.dto;


import com.stockmanagement.stockmanagement.common.enums.Role;
import com.stockmanagement.stockmanagement.common.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record KareegarResponse(

        Long id,
        String kareegarCode,
        String kareegarName,
        String mobileNo,
        String emailId,
        String address,
        Role role,
        Status status,
        LocalDate joinDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version,
        boolean deleted,
        LocalDateTime deletedAt
) {
}
