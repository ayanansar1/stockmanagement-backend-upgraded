package com.stockmanagement.stockmanagement.salesinvoice.service.imp;

import com.stockmanagement.stockmanagement.buyer.entity.Buyer;
import com.stockmanagement.stockmanagement.buyer.repository.BuyerRepository;
import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import com.stockmanagement.stockmanagement.productsell.service.ProductSellService;
import com.stockmanagement.stockmanagement.salesinvoice.dto.AddInvoiceItemRequest;
import com.stockmanagement.stockmanagement.salesinvoice.dto.CreateInvoiceRequest;
import com.stockmanagement.stockmanagement.salesinvoice.dto.SalesInvoiceResponse;
import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoice;
import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoiceItem;
import com.stockmanagement.stockmanagement.salesinvoice.enums.InvoiceStatus;
import com.stockmanagement.stockmanagement.salesinvoice.mapper.SalesInvoiceMapper;
import com.stockmanagement.stockmanagement.salesinvoice.repository.SalesInvoiceItemRepository;
import com.stockmanagement.stockmanagement.salesinvoice.repository.SalesInvoiceRepository;
import com.stockmanagement.stockmanagement.salesinvoice.service.SalesInvoiceService;
import com.stockmanagement.stockmanagement.stockbalance.service.ProductStockBalanceService;
import com.stockmanagement.stockmanagement.stockmovement.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesInvoiceServiceImpl
        implements SalesInvoiceService {

    private final SalesInvoiceRepository invoiceRepository;
    private final SalesInvoiceItemRepository itemRepository;

    private final BuyerRepository buyerRepository;

    private final BusinessCodeService businessCodeService;

    private final ProductSellService productSellService;

    private final StockMovementService stockMovementService;

    private final ProductStockBalanceService productStockBalanceService;

    private final SalesInvoiceMapper invoiceMapper;


    // =========================================================
    // CREATE DRAFT
    // =========================================================

    @Override
    @Transactional
    public SalesInvoiceResponse createDraft(
            CreateInvoiceRequest request
    ) {

        Buyer buyer =
                buyerRepository
                        .findByBuyerCodeAndDeletedFalse(
                                request.buyerCode()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Buyer not found: "
                                                + request.buyerCode()
                                )
                        );

        if (buyer.getStatus() != Status.ACTIVE
                || buyer.isDeleted()) {

            throw new IllegalStateException(
                    "Buyer is not active: "
                            + buyer.getBuyerCode()
            );
        }

        SalesInvoice invoice =
                SalesInvoice.builder()
                        .invoiceCode(
                                businessCodeService.generateCode(
                                        CodeType.INVOICE
                                )
                        )
                        .buyer(buyer)
                        .invoiceDate(request.invoiceDate())
                        .subtotal(BigDecimal.ZERO)
                        .discount(BigDecimal.ZERO)
                        .tax(BigDecimal.ZERO)
                        .grandTotal(BigDecimal.ZERO)
                        .status(InvoiceStatus.DRAFT)
                        .build();

        SalesInvoice saved =
                invoiceRepository.save(invoice);

        return invoiceMapper.toResponse(saved);
    }


    // =========================================================
    // ADD ITEM
    // =========================================================

    @Override
    @Transactional
    public SalesInvoiceResponse addItem(
            String invoiceCode,
            AddInvoiceItemRequest request
    ) {

        SalesInvoice invoice =
                getInvoice(invoiceCode);

        validateDraft(invoice);


        // -----------------------------------------------------
        // 1. Create ProductSell
        // -----------------------------------------------------

        ProductSell productSell =
                productSellService.createForInvoice(
                        invoice,
                        request
                );


        // -----------------------------------------------------
        // 2. Create OUT Movement
        // -----------------------------------------------------

        stockMovementService.createOutMovement(
                productSell
        );


        // -----------------------------------------------------
        // 3. Deduct Stock
        // -----------------------------------------------------

        productStockBalanceService.deductStock(
                productSell
        );


        // -----------------------------------------------------
        // 4. Calculate Amount
        // -----------------------------------------------------

        BigDecimal amount =
                request.quantity()
                        .multiply(request.rate());


        // -----------------------------------------------------
        // 5. Create Invoice Item
        // -----------------------------------------------------

        SalesInvoiceItem item =
                SalesInvoiceItem.builder()
                        .invoice(invoice)
                        .productSell(productSell)
                        .quantity(request.quantity())
                        .unit(request.unit())
                        .rate(request.rate())
                        .amount(amount)
                        .build();

        itemRepository.save(item);


        // -----------------------------------------------------
        // 6. Update Subtotal
        // -----------------------------------------------------

        BigDecimal newSubtotal =
                invoice.getSubtotal()
                        .add(amount);

        invoice.setSubtotal(newSubtotal);


        // -----------------------------------------------------
        // 7. Update Grand Total
        // -----------------------------------------------------

        BigDecimal grandTotal =
                newSubtotal
                        .subtract(invoice.getDiscount())
                        .add(invoice.getTax());

        invoice.setGrandTotal(grandTotal);


        // -----------------------------------------------------
        // 8. Save Invoice
        // -----------------------------------------------------

        SalesInvoice saved =
                invoiceRepository.save(invoice);


        // -----------------------------------------------------
        // 9. Load Items
        // -----------------------------------------------------

        List<SalesInvoiceItem> items =
                itemRepository.findAllByInvoice(saved);

        saved.setItems(items);


        // -----------------------------------------------------
        // 10. Response
        // -----------------------------------------------------

        return invoiceMapper.toResponse(saved);
    }


    // =========================================================
    // FINALIZE
    // =========================================================

    @Override
    @Transactional
    public SalesInvoiceResponse finalizeInvoice(
            String invoiceCode
    ) {

        SalesInvoice invoice =
                getInvoice(invoiceCode);

        validateDraft(invoice);

        List<SalesInvoiceItem> items =
                itemRepository.findAllByInvoice(invoice);

        if (items.isEmpty()) {
            throw new IllegalStateException(
                    "Cannot finalize empty invoice"
            );
        }

        invoice.setStatus(
                InvoiceStatus.FINALIZED
        );

        SalesInvoice saved =
                invoiceRepository.save(invoice);

        saved.setItems(items);

        return invoiceMapper.toResponse(saved);
    }


    // =========================================================
    // GET BY CODE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public SalesInvoiceResponse getByCode(
            String invoiceCode
    ) {

        SalesInvoice invoice =
                getInvoice(invoiceCode);

        List<SalesInvoiceItem> items =
                itemRepository.findAllByInvoice(invoice);

        invoice.setItems(items);

        return invoiceMapper.toResponse(invoice);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<SalesInvoiceResponse> getAll() {

        return invoiceRepository
                .findAllByOrderByInvoiceDateDescCreatedAtDesc()
                .stream()
                .map(invoiceMapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET INVOICE
    // =========================================================

    private SalesInvoice getInvoice(
            String invoiceCode
    ) {

        return invoiceRepository
                .findByInvoiceCode(invoiceCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Invoice not found: "
                                        + invoiceCode
                        )
                );
    }


    // =========================================================
    // VALIDATE DRAFT
    // =========================================================

    private void validateDraft(
            SalesInvoice invoice
    ) {

        if (invoice.getStatus()
                != InvoiceStatus.DRAFT) {

            throw new IllegalStateException(
                    "Invoice is not in DRAFT status"
            );
        }
    }
}

//package com.stockmanagement.stockmanagement.salesinvoice.service.imp;
//
//
//import com.stockmanagement.stockmanagement.buyer.entity.Buyer;
//import com.stockmanagement.stockmanagement.buyer.repository.BuyerRepository;
//import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
//import com.stockmanagement.stockmanagement.common.generator.CodeType;
//import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
//import com.stockmanagement.stockmanagement.productsell.dto.ProductSellResponse;
//import com.stockmanagement.stockmanagement.productsell.service.ProductSellService;
//import com.stockmanagement.stockmanagement.salesinvoice.dto.*;
//import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoice;
//import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoiceItem;
//import com.stockmanagement.stockmanagement.salesinvoice.enums.InvoiceStatus;
//import com.stockmanagement.stockmanagement.salesinvoice.mapper.SalesInvoiceMapper;
//import com.stockmanagement.stockmanagement.salesinvoice.repository.SalesInvoiceItemRepository;
//import com.stockmanagement.stockmanagement.salesinvoice.repository.SalesInvoiceRepository;
//import com.stockmanagement.stockmanagement.salesinvoice.service.SalesInvoiceService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class SalesInvoiceServiceImpl
//        implements SalesInvoiceService {
//
//    private final SalesInvoiceRepository invoiceRepository;
//    private final SalesInvoiceItemRepository itemRepository;
//
//    private final BuyerRepository buyerRepository;
//
//    private final BusinessCodeService businessCodeService;
//
//    private final ProductSellService productSellService;
//
//    private final SalesInvoiceMapper invoiceMapper;
//
//    @Override
//    @Transactional
//    public SalesInvoiceResponse createDraft(
//            CreateInvoiceRequest request
//    ) {
//
//        Buyer buyer =
//                buyerRepository
//                        .findByBuyerCodeAndDeletedFalse(
//                                request.buyerCode()
//                        )
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Buyer not found: "
//                                                + request.buyerCode()
//                                )
//                        );
//
//        if (buyer.getStatus() == null
//                || buyer.getStatus().name().equals("INACTIVE")) {
//
//            throw new IllegalStateException(
//                    "Buyer is not active: "
//                            + buyer.getBuyerCode()
//            );
//        }
//
//        SalesInvoice invoice =
//                SalesInvoice.builder()
//                        .invoiceCode(
//                                businessCodeService.generateCode(
//                                        CodeType.INVOICE
//                                )
//                        )
//                        .buyer(buyer)
//                        .invoiceDate(request.invoiceDate())
//                        .subtotal(BigDecimal.ZERO)
//                        .discount(BigDecimal.ZERO)
//                        .tax(BigDecimal.ZERO)
//                        .grandTotal(BigDecimal.ZERO)
//                        .status(InvoiceStatus.DRAFT)
//                        .build();
//
//        SalesInvoice saved =
//                invoiceRepository.save(invoice);
//
//        return invoiceMapper.toResponse(saved);
//    }
//
//    @Override
//    @Transactional
//    public SalesInvoiceResponse addItem(
//            String invoiceCode,
//            AddInvoiceItemRequest request
//    ) {
//
//        SalesInvoice invoice =
//                getInvoice(invoiceCode);
//
//        validateDraft(invoice);
//
//        /*
//         * ProductSell creation:
//         * - Kareegar validation
//         * - Machine validation
//         * - Product mapping
//         * - Bana mapping
//         * - Design mapping
//         * - ProductSell creation
//         */
//        ProductSellResponse productSell =
//                productSellService.createForInvoice(
//                        invoice,
//                        request
//                );
//
//        /*
//         * Invoice amount
//         */
//        BigDecimal amount =
//                request.quantity()
//                        .multiply(request.rate());
//
//        SalesInvoiceItem item =
//                SalesInvoiceItem.builder()
//                        .invoice(invoice)
//                        .quantity(request.quantity())
//                        .unit(request.unit())
//                        .rate(request.rate())
//                        .amount(amount)
//                        .build();
//
//        /*
//         * ProductSell ko entity chahiye.
//         * Isliye yahan actual ProductSell entity fetch karna better hai.
//         */
//        throw new UnsupportedOperationException(
//                "Connect ProductSell entity lookup here"
//        );
//    }
//
//    @Override
//    @Transactional
//    public SalesInvoiceResponse finalizeInvoice(
//            String invoiceCode
//    ) {
//
//        SalesInvoice invoice =
//                getInvoice(invoiceCode);
//
//        validateDraft(invoice);
//
//        List<SalesInvoiceItem> items =
//                itemRepository.findAllByInvoice(invoice);
//
//        if (items.isEmpty()) {
//            throw new IllegalStateException(
//                    "Cannot finalize empty invoice"
//            );
//        }
//
//        invoice.setStatus(InvoiceStatus.FINALIZED);
//
//        SalesInvoice saved =
//                invoiceRepository.save(invoice);
//
//        return invoiceMapper.toResponse(saved);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public SalesInvoiceResponse getByCode(
//            String invoiceCode
//    ) {
//
//        SalesInvoice invoice =
//                getInvoice(invoiceCode);
//
//        List<SalesInvoiceItem> items =
//                itemRepository.findAllByInvoice(invoice);
//
//        invoice.setItems(items);
//
//        return invoiceMapper.toResponse(invoice);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<SalesInvoiceResponse> getAll() {
//
//        return invoiceRepository
//                .findAllByOrderByInvoiceDateDescCreatedAtDesc()
//                .stream()
//                .map(invoiceMapper::toResponse)
//                .toList();
//    }
//
//    private SalesInvoice getInvoice(
//            String invoiceCode
//    ) {
//
//        return invoiceRepository
//                .findByInvoiceCode(invoiceCode)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "Invoice not found: "
//                                        + invoiceCode
//                        )
//                );
//    }
//
//    private void validateDraft(
//            SalesInvoice invoice
//    ) {
//
//        if (invoice.getStatus() != InvoiceStatus.DRAFT) {
//            throw new IllegalStateException(
//                    "Invoice is not in DRAFT status"
//            );
//        }
//    }
//}