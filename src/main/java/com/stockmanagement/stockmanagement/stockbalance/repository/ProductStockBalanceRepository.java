package com.stockmanagement.stockmanagement.stockbalance.repository;


import com.stockmanagement.stockmanagement.common.enums.StockUnit;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.entity.BanaKareegar;
import com.stockmanagement.stockmanagement.mapping.designKareegar.entity.DesignKareegar;
import com.stockmanagement.stockmanagement.mapping.productKareegar.entity.ProductKareegar;

import com.stockmanagement.stockmanagement.stockbalance.entity.ProductStockBalance;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductStockBalanceRepository
        extends JpaRepository<ProductStockBalance, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT b
        FROM ProductStockBalance b
        WHERE b.kareegar = :kareegar
          AND b.machine = :machine
          AND b.productKareegar = :productKareegar
          AND b.banaKareegar = :banaKareegar
          AND b.designKareegar = :designKareegar
          AND b.unit = :unit
    """)
    Optional<ProductStockBalance> findForUpdate(

            @Param("kareegar")
            Kareegar kareegar,

            @Param("machine")
            Machine machine,

            @Param("productKareegar")
            ProductKareegar productKareegar,

            @Param("banaKareegar")
            BanaKareegar banaKareegar,

            @Param("designKareegar")
            DesignKareegar designKareegar,

            @Param("unit")
            StockUnit unit
    );

    Optional<ProductStockBalance>
    findByStockBalanceCode(String stockBalanceCode);

    @Query("""
    SELECT b.unit, COALESCE(SUM(b.quantity), 0)
    FROM ProductStockBalance b
    WHERE b.deleted = false
    GROUP BY b.unit
    ORDER BY b.unit
    """)
    List<Object[]> getStockQuantityGroupedByUnit();
}