package com.stockmanagement.stockmanagement.dashboard.dto;

import java.math.BigDecimal;

public record StockMovementSummary(
        String movementType,
        String unit,
        BigDecimal quantity
) {
}