package com.stockmanagement.stockmanagement.dashboard.service.imp;


import com.stockmanagement.stockmanagement.bana.repository.BanaRepository;
import com.stockmanagement.stockmanagement.buyer.repository.BuyerRepository;
import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.dashboard.dto.DashboardResponse;
import com.stockmanagement.stockmanagement.dashboard.dto.EntityStatusSummary;
import com.stockmanagement.stockmanagement.dashboard.dto.RecentActivityResponse;
import com.stockmanagement.stockmanagement.dashboard.dto.StockMovementSummary;
import com.stockmanagement.stockmanagement.dashboard.service.DashboardService;
import com.stockmanagement.stockmanagement.design.repository.DesignRepository;
import com.stockmanagement.stockmanagement.kareegar.repository.KareegarRepository;
import com.stockmanagement.stockmanagement.machine.repository.MachineRepository;
import com.stockmanagement.stockmanagement.product.repository.ProductRepository;
import com.stockmanagement.stockmanagement.productreturn.entity.ProductReturn;
import com.stockmanagement.stockmanagement.productreturn.repository.ProductReturnRepository;
import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoice;
import com.stockmanagement.stockmanagement.salesinvoice.enums.InvoiceStatus;
import com.stockmanagement.stockmanagement.salesinvoice.repository.SalesInvoiceRepository;
import com.stockmanagement.stockmanagement.stockbalance.repository.ProductStockBalanceRepository;
import com.stockmanagement.stockmanagement.stockmovement.entity.StockMovement;
import com.stockmanagement.stockmanagement.stockmovement.repository.StockMovementRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final BuyerRepository buyerRepository;
    private final KareegarRepository kareegarRepository;
    private final MachineRepository machineRepository;
    private final BanaRepository banaRepository;
    private final DesignRepository designRepository;
    private final ProductRepository productRepository;

    private final SalesInvoiceRepository salesInvoiceRepository;
    private final ProductStockBalanceRepository stockBalanceRepository;
    private final StockMovementRepository stockMovementRepository;
    private final ProductReturnRepository productReturnRepository;

    @Override
    public DashboardResponse getDashboard() {

        LocalDate today = LocalDate.now();
        LocalDate monthStart = today.withDayOfMonth(1);
        LocalDate nextMonthStart = monthStart.plusMonths(1);

        // Phase 1: Entity counts
        DashboardResponse.EntityCounts entities =
                new DashboardResponse.EntityCounts(
                        buyerRepository.countByDeletedFalse(),
                        kareegarRepository.countByDeletedFalse(),
                        machineRepository.countByDeletedFalse(),
                        banaRepository.countByDeletedFalse(),
                        designRepository.countByDeletedFalse(),
                        productRepository.countByDeletedFalse()
                );

        // Phase 1: Sales summary
        BigDecimal todaySales = safeAmount(
                salesInvoiceRepository.sumSalesByStatusAndDate(
                        InvoiceStatus.FINALIZED, today
                )
        );

        BigDecimal monthSales = safeAmount(
                salesInvoiceRepository.sumSalesByStatusAndDateRange(
                        InvoiceStatus.FINALIZED,
                        monthStart,
                        nextMonthStart
                )
        );

        long todayInvoices =
                salesInvoiceRepository.countInvoicesByStatusAndDate(
                        InvoiceStatus.FINALIZED, today
                );

        DashboardResponse.SalesSummary sales =
                new DashboardResponse.SalesSummary(
                        todaySales,
                        monthSales,
                        todayInvoices
                );

        // Phase 1: Stock grouped by unit
        List<DashboardResponse.StockUnitSummary> stockByUnit =
                stockBalanceRepository.getStockQuantityGroupedByUnit()
                        .stream()
                        .map(row -> new DashboardResponse.StockUnitSummary(
                                String.valueOf(row[0]),
                                safeAmount((BigDecimal) row[1])
                        ))
                        .toList();

        // Phase 1: Daily sales for current month
        Map<LocalDate, BigDecimal> dailySalesMap =
                salesInvoiceRepository.findDailySales(
                                InvoiceStatus.FINALIZED,
                                monthStart,
                                nextMonthStart
                        )
                        .stream()
                        .collect(java.util.stream.Collectors.toMap(
                                row -> (LocalDate) row[0],
                                row -> safeAmount((BigDecimal) row[1])
                        ));

        List<DashboardResponse.DailySalesSummary> dailySales =
                new ArrayList<>();

        for (LocalDate date = monthStart;
             date.isBefore(nextMonthStart);
             date = date.plusDays(1)) {

            dailySales.add(
                    new DashboardResponse.DailySalesSummary(
                            date,
                            dailySalesMap.getOrDefault(
                                    date, BigDecimal.ZERO
                            )
                    )
            );
        }

        // Phase 2: Active/inactive summary
        List<EntityStatusSummary> entityStatus = List.of(
                new EntityStatusSummary(
                        "BUYER",
                        buyerRepository.countByStatusAndDeletedFalse(Status.ACTIVE),
                        buyerRepository.countByStatusAndDeletedFalse(Status.INACTIVE)
                ),
                new EntityStatusSummary(
                        "KAREEGAR",
                        kareegarRepository.countByDeletedFalseAndStatus(Status.ACTIVE),
                        kareegarRepository.countByDeletedFalseAndStatus(Status.INACTIVE)
                ),
                new EntityStatusSummary(
                        "MACHINE",
                        machineRepository.countByMachineStatusAndDeletedFalse(Status.ACTIVE),
                        machineRepository.countByMachineStatusAndDeletedFalse(Status.INACTIVE)
                ),
                new EntityStatusSummary(
                        "BANA",
                        banaRepository.countByStatusAndDeletedFalse(Status.ACTIVE),
                        banaRepository.countByStatusAndDeletedFalse(Status.INACTIVE)
                ),
                new EntityStatusSummary(
                        "DESIGN",
                        designRepository.countByStatusAndDeletedFalse(Status.ACTIVE),
                        designRepository.countByStatusAndDeletedFalse(Status.INACTIVE)
                ),
                new EntityStatusSummary(
                        "PRODUCT",
                        productRepository.countByStatusAndDeletedFalse(Status.ACTIVE),
                        productRepository.countByStatusAndDeletedFalse(Status.INACTIVE)
                )
        );

        // Phase 2: Current month's stock movement summary
        List<StockMovementSummary> stockMovements =
                stockMovementRepository.getMovementSummary(
                                monthStart, nextMonthStart
                        )
                        .stream()
                        .map(row -> new StockMovementSummary(
                                String.valueOf(row[0]),
                                String.valueOf(row[1]),
                                safeAmount((BigDecimal) row[2])
                        ))
                        .toList();

        // Phase 2: Recent activities from three sources
        Stream<RecentActivityResponse> movementActivities =
                stockMovementRepository
                        .findTop10ByDeletedFalseOrderByMovementDateDescCreatedAtDesc()
                        .stream()
                        .map(this::toMovementActivity);

        Stream<RecentActivityResponse> returnActivities =
                productReturnRepository
                        .findTop10ByDeletedFalseOrderByReturnDateDescCreatedAtDesc()
                        .stream()
                        .map(this::toReturnActivity);

        Stream<RecentActivityResponse> invoiceActivities =
                salesInvoiceRepository
                        .findTop10ByDeletedFalseOrderByInvoiceDateDescCreatedAtDesc()
                        .stream()
                        .map(this::toInvoiceActivity);

        List<RecentActivityResponse> recentActivities =
                Stream.of(
                                movementActivities,
                                returnActivities,
                                invoiceActivities
                        )
                        .flatMap(stream -> stream)
                        .sorted(
                                Comparator.comparing(
                                                RecentActivityResponse::eventDate,
                                                Comparator.nullsLast(
                                                        Comparator.reverseOrder()
                                                )
                                        )
                                        .thenComparing(
                                                RecentActivityResponse::createdAt,
                                                Comparator.nullsLast(
                                                        Comparator.reverseOrder()
                                                )
                                        )
                        )
                        .limit(10)
                        .toList();

        return new DashboardResponse(
                entities,
                sales,
                stockByUnit,
                dailySales,
                entityStatus,
                stockMovements,
                recentActivities
        );
    }

    private RecentActivityResponse toMovementActivity(
            StockMovement movement
    ) {
        return new RecentActivityResponse(
                "STOCK_MOVEMENT",
                movement.getMovementCode(),
                movement.getMovementDate(),
                movement.getCreatedAt(),
                movement.getMovementType().name(),
                movement.getQuantity(),
                movement.getUnit().name(),
                null
        );
    }

    private RecentActivityResponse toReturnActivity(
            ProductReturn productReturn
    ) {
        return new RecentActivityResponse(
                "PRODUCT_RETURN",
                productReturn.getProductReturnCode(),
                productReturn.getReturnDate(),
                productReturn.getCreatedAt(),
                productReturn.getStatus().name(),
                productReturn.getReturnQuantity(),
                productReturn.getUnit().name(),
                null
        );
    }

    private RecentActivityResponse toInvoiceActivity(
            SalesInvoice invoice
    ) {
        return new RecentActivityResponse(
                "SALES_INVOICE",
                invoice.getInvoiceCode(),
                invoice.getInvoiceDate(),
                invoice.getCreatedAt(),
                invoice.getStatus().name(),
                null,
                null,
                safeAmount(invoice.getGrandTotal())
        );
    }

    private BigDecimal safeAmount(BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount;
    }
}