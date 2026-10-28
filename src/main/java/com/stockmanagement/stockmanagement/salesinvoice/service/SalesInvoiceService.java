package com.stockmanagement.stockmanagement.salesinvoice.service;

import com.stockmanagement.stockmanagement.salesinvoice.dto.AddInvoiceItemRequest;
import com.stockmanagement.stockmanagement.salesinvoice.dto.CreateInvoiceRequest;
import com.stockmanagement.stockmanagement.salesinvoice.dto.SalesInvoiceResponse;

import java.util.List;

public interface SalesInvoiceService {

    SalesInvoiceResponse createDraft(
            CreateInvoiceRequest request
    );

    SalesInvoiceResponse getByCode(
            String invoiceCode
    );

    List<SalesInvoiceResponse> getAll();

    SalesInvoiceResponse addItem(
            String invoiceCode,
            AddInvoiceItemRequest request
    );

    SalesInvoiceResponse finalizeInvoice(
            String invoiceCode
    );
}