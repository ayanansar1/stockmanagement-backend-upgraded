package com.stockmanagement.stockmanagement.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record RecentActivityResponse(
        String activityType,
        String code,
        LocalDate eventDate,
        LocalDateTime createdAt,
        String status,
        BigDecimal quantity,
        String unit,
        BigDecimal amount
) {
}