package com.stockmanagement.stockmanagement.productreturn.service.imp;

import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;


import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
import com.stockmanagement.stockmanagement.productreturn.dto.ProductReturnCreateRequest;
import com.stockmanagement.stockmanagement.productreturn.dto.ProductReturnResponse;
import com.stockmanagement.stockmanagement.productreturn.entity.ProductReturn;
import com.stockmanagement.stockmanagement.productreturn.mapper.ProductReturnMapper;
import com.stockmanagement.stockmanagement.productreturn.repository.ProductReturnRepository;
import com.stockmanagement.stockmanagement.productreturn.service.ProductReturnService;

import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import com.stockmanagement.stockmanagement.productsell.repository.ProductSellRepository;

import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoice;
import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoiceItem;
import com.stockmanagement.stockmanagement.salesinvoice.enums.InvoiceStatus;
import com.stockmanagement.stockmanagement.salesinvoice.repository.SalesInvoiceItemRepository;
import com.stockmanagement.stockmanagement.salesinvoice.repository.SalesInvoiceRepository;

import com.stockmanagement.stockmanagement.stockbalance.service.ProductStockBalanceService;
import com.stockmanagement.stockmanagement.stockmovement.service.StockMovementService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductReturnServiceImpl implements ProductReturnService {

    private final ProductReturnRepository productReturnRepository;
    private final ProductSellRepository productSellRepository;
    private final SalesInvoiceRepository salesInvoiceRepository;
    private final SalesInvoiceItemRepository salesInvoiceItemRepository;

    private final BusinessCodeService businessCodeService;
    private final ProductReturnMapper productReturnMapper;

    private final StockMovementService stockMovementService;
    private final ProductStockBalanceService productStockBalanceService;

    @Override
    @Transactional
    public List<ProductReturnResponse> create(
            ProductReturnCreateRequest request) {

        // 1. Find invoice
        SalesInvoice invoice = salesInvoiceRepository
                .findByInvoiceCodeAndDeletedFalse(request.invoiceCode())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sales invoice not found: " + request.invoiceCode()
                ));

        // 2. Only finalized invoices can be returned
        if (invoice.getStatus() != InvoiceStatus.FINALIZED) {
            throw new IllegalStateException(
                    "Return is allowed only for FINALIZED invoices"
            );
        }

        // 3. Reject duplicate sale codes
        Set<String> uniqueCodes = new HashSet<>();

        for (ProductReturnCreateRequest.ProductReturnItemRequest item
                : request.items()) {

            if (!uniqueCodes.add(item.productSellCode())) {
                throw new IllegalArgumentException(
                        "Duplicate product sell code: "
                                + item.productSellCode()
                );
            }
        }

        List<ProductReturnResponse> responses = new ArrayList<>();

        /*
         * Sort codes before locking to reduce the risk of deadlocks
         * when concurrent requests return overlapping invoice items.
         */
        List<ProductReturnCreateRequest.ProductReturnItemRequest> items =
                request.items().stream()
                        .sorted((a, b) ->
                                a.productSellCode().compareTo(b.productSellCode()))
                        .toList();

        // 4. Process every item inside the same transaction
        for (ProductReturnCreateRequest.ProductReturnItemRequest item
                : items) {

            ProductSell productSell = productSellRepository
                    .findByProductSellCodeForUpdate(item.productSellCode())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product sell not found: " + item.productSellCode()
                    ));

            // 5. Validate sale record
            if (productSell.isDeleted()) {
                throw new IllegalStateException(
                        "Deleted product sale cannot be returned: "
                                + item.productSellCode()
                );
            }

            if (productSell.getStatus() != Status.ACTIVE) {
                throw new IllegalStateException(
                        "Inactive product sale cannot be returned: "
                                + item.productSellCode()
                );
            }

            // 6. Verify sale belongs to selected invoice
            SalesInvoiceItem invoiceItem = salesInvoiceItemRepository
                    .findByInvoiceAndProductSell(invoice, productSell)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Product sell " + item.productSellCode()
                                    + " does not belong to invoice "
                                    + request.invoiceCode()
                    ));

            // 7. Calculate previous returns
            BigDecimal alreadyReturned =
                    productReturnRepository.getTotalReturnedQuantity(
                            productSell,
                            Status.ACTIVE
                    );

            BigDecimal soldQuantity = invoiceItem.getQuantity();

            BigDecimal remainingQuantity =
                    soldQuantity.subtract(alreadyReturned);

            if (remainingQuantity.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalStateException(
                        "Previously returned quantity exceeds sold quantity for "
                                + item.productSellCode()
                );
            }

            // 8. Validate requested quantity
            BigDecimal requestedQuantity = item.returnQuantity();

            if (requestedQuantity.compareTo(remainingQuantity) > 0) {
                throw new IllegalArgumentException(
                        "Return quantity exceeds remaining quantity for "
                                + item.productSellCode()
                                + ". Sold: " + soldQuantity
                                + ", Already returned: " + alreadyReturned
                                + ", Remaining: " + remainingQuantity
                );
            }

            // 9. Build return record
            ProductReturn productReturn = ProductReturn.builder()
                    .productReturnCode(
                            businessCodeService.generateCode(
                                    CodeType.PRODUCT_RETURN
                            )
                    )
                    .productSell(productSell)
                    .returnQuantity(requestedQuantity)
                    .unit(invoiceItem.getUnit())
                    .returnDate(request.returnDate())
                    .returnReason(request.returnReason())
                    .remarks(request.remarks())
                    .status(Status.ACTIVE)
                    .build();

            // 10. Save return
            ProductReturn savedReturn =
                    productReturnRepository.save(productReturn);

            /*
             * 11. Record stock movement and increase stock.
             * Both must participate in this same transaction.
             */
            stockMovementService.createReturnMovement(savedReturn);

            productStockBalanceService.addReturnedStock(savedReturn);

            // 12. Map response
            responses.add(
                    productReturnMapper.toResponse(savedReturn)
            );
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductReturnResponse getByCode(String productReturnCode) {

        ProductReturn productReturn = productReturnRepository
                .findByProductReturnCodeAndDeletedFalse(productReturnCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product return not found: " + productReturnCode
                ));

        return productReturnMapper.toResponse(productReturn);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReturnResponse> getAll() {

        return productReturnRepository
                .findAllByDeletedFalseOrderByReturnDateDescCreatedAtDesc()
                .stream()
                .map(productReturnMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductReturnResponse> getByProductSell(
            String productSellCode) {

        ProductSell productSell = productSellRepository
                .findByProductSellCode(productSellCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product sell not found: " + productSellCode
                ));

        return productReturnRepository
                .findAllByProductSellAndDeletedFalseOrderByReturnDateDescCreatedAtDesc(
                        productSell
                )
                .stream()
                .map(productReturnMapper::toResponse)
                .toList();
    }
}