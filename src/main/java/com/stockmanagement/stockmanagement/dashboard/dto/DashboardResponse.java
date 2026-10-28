package com.stockmanagement.stockmanagement.dashboard.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DashboardResponse(
        EntityCounts entities,
        SalesSummary sales,
        List<StockUnitSummary> stockByUnit,
        List<DailySalesSummary> dailySales,
        List<EntityStatusSummary> entityStatus,
        List<StockMovementSummary> stockMovements,
        List<RecentActivityResponse> recentActivities
) {

    public record EntityCounts(
            long buyers,
            long kareegars,
            long machines,
            long banas,
            long designs,
            long products
    ) {
    }

    public record SalesSummary(
            BigDecimal todaySales,
            BigDecimal monthSales,
            long todayFinalizedInvoices

    ) {
    }

    public record StockUnitSummary(
            String unit,
            BigDecimal quantity
    ) {
    }

    public record DailySalesSummary(
            LocalDate date,
            BigDecimal sales
    ) {
    }
}