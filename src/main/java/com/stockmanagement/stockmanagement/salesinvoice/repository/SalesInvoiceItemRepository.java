package com.stockmanagement.stockmanagement.salesinvoice.repository;

import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoice;
import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalesInvoiceItemRepository
        extends JpaRepository<SalesInvoiceItem, Long> {

    List<SalesInvoiceItem> findAllByInvoice(
            SalesInvoice invoice
    );

    boolean existsByProductSell(
            ProductSell productSell
    );

    Optional<SalesInvoiceItem> findByInvoiceAndProductSell(
            SalesInvoice invoice,
            ProductSell productSell
    );
}