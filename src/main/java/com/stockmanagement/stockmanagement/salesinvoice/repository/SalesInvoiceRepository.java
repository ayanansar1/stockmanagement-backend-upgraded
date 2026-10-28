package com.stockmanagement.stockmanagement.salesinvoice.repository;

import com.stockmanagement.stockmanagement.buyer.entity.Buyer;
import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoice;
import com.stockmanagement.stockmanagement.salesinvoice.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SalesInvoiceRepository
        extends JpaRepository<SalesInvoice, Long> {

    Optional<SalesInvoice> findByInvoiceCode(
            String invoiceCode
    );

    List<SalesInvoice>
    findAllByOrderByInvoiceDateDescCreatedAtDesc();

    List<SalesInvoice>
    findAllByBuyerOrderByInvoiceDateDescCreatedAtDesc(
            Buyer buyer
    );

    Optional<SalesInvoice> findByInvoiceCodeAndDeletedFalse(
            String invoiceCode
    );


    long countByDeletedFalse();

    @Query("""
    SELECT COUNT(i)
    FROM SalesInvoice i
    WHERE i.deleted = false
      AND i.status = :status
      AND i.invoiceDate = :date
    """)
    long countInvoicesByStatusAndDate(
            @Param("status") InvoiceStatus status,
            @Param("date") LocalDate date
    );

    @Query("""
    SELECT COALESCE(SUM(i.grandTotal), 0)
    FROM SalesInvoice i
    WHERE i.deleted = false
      AND i.status = :status
      AND i.invoiceDate = :date
    """)
    BigDecimal sumSalesByStatusAndDate(
            @Param("status") InvoiceStatus status,
            @Param("date") LocalDate date
    );

    @Query("""
    SELECT COALESCE(SUM(i.grandTotal), 0)
    FROM SalesInvoice i
    WHERE i.deleted = false
      AND i.status = :status
      AND i.invoiceDate >= :startDate
      AND i.invoiceDate < :endDate
    """)
    BigDecimal sumSalesByStatusAndDateRange(
            @Param("status") InvoiceStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("""
    SELECT i.invoiceDate, COALESCE(SUM(i.grandTotal), 0)
    FROM SalesInvoice i
    WHERE i.deleted = false
      AND i.status = :status
      AND i.invoiceDate >= :startDate
      AND i.invoiceDate < :endDate
    GROUP BY i.invoiceDate
    ORDER BY i.invoiceDate
    """)
    List<Object[]> findDailySales(
            @Param("status") InvoiceStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<SalesInvoice>
    findTop10ByDeletedFalseOrderByInvoiceDateDescCreatedAtDesc();

}