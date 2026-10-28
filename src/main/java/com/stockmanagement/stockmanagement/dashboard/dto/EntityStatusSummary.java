package com.stockmanagement.stockmanagement.dashboard.dto;


public record EntityStatusSummary(
        String entity,
        long active,
        long inactive
) {
}