package com.stockmanagement.stockmanagement.productsell.repository;

import com.stockmanagement.stockmanagement.buyer.entity.Buyer;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductSellRepository
        extends JpaRepository<ProductSell, Long> {

    Optional<ProductSell> findByProductSellCode(
            String productSellCode
    );

    List<ProductSell>
    findAllByOrderBySellDateDescCreatedAtDesc();

    List<ProductSell>
    findAllByKareegarOrderBySellDateDescCreatedAtDesc(
            Kareegar kareegar
    );

    List<ProductSell>
    findAllByMachineOrderBySellDateDescCreatedAtDesc(
            Machine machine
    );

    List<ProductSell>
    findAllByBuyerOrderBySellDateDescCreatedAtDesc(
            Buyer buyer
    );

    boolean existsByProductSellCode(String productSellCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT ps
    FROM ProductSell ps
    WHERE ps.productSellCode = :code
      AND ps.deleted = false
    """)
    Optional<ProductSell> findByProductSellCodeForUpdate(
            @Param("code") String code
    );
    // Use when the caller should not retrieve deleted sales.
    Optional<ProductSell> findByProductSellCodeAndDeletedFalse(
            String productSellCode
    );

}