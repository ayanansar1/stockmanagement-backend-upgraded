package com.stockmanagement.stockmanagement.productreturn.repository;

import com.stockmanagement.stockmanagement.productreturn.entity.ProductReturn;
import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import com.stockmanagement.stockmanagement.common.enums.Status;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductReturnRepository
        extends JpaRepository<ProductReturn, Long> {

    Optional<ProductReturn> findByProductReturnCodeAndDeletedFalse(
            String productReturnCode
    );

    List<ProductReturn> findAllByDeletedFalseOrderByReturnDateDescCreatedAtDesc();

    List<ProductReturn>
    findAllByProductSellAndDeletedFalseOrderByReturnDateDescCreatedAtDesc(
            ProductSell productSell
    );
    List<ProductReturn>
    findTop10ByDeletedFalseOrderByReturnDateDescCreatedAtDesc();
    @Query("""
        SELECT COALESCE(SUM(r.returnQuantity), 0)
        FROM ProductReturn r
        WHERE r.productSell = :productSell
          AND r.deleted = false
          AND r.status = :status
        """)
    BigDecimal getTotalReturnedQuantity(
            @Param("productSell") ProductSell productSell,
            @Param("status") Status status
    );
}