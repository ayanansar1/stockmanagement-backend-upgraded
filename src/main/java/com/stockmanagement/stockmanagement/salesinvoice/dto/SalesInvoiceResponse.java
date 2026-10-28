package com.stockmanagement.stockmanagement.salesinvoice.dto;

import com.stockmanagement.stockmanagement.salesinvoice.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SalesInvoiceResponse(

        String invoiceCode,

        String buyerCode,

        String buyerName,

        LocalDate invoiceDate,

        BigDecimal subtotal,

        BigDecimal discount,

        BigDecimal tax,

        BigDecimal grandTotal,

        List<SalesInvoiceItemResponse> items,

        InvoiceStatus status,

        LocalDateTime createdAt,

        LocalDateTime updatedAt,

        Long version
) {
}