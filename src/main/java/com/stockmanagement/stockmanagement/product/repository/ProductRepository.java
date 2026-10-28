package com.stockmanagement.stockmanagement.product.repository;

import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.product.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository
        extends JpaRepository<Product, Long> {

    Optional<Product> findByProductCodeAndDeletedFalse(
            String productCode
    );

    Optional<Product> findByProductCodeAndDeletedTrue(
            String productCode
    );

    List<Product> findAllByDeletedFalse();

    List<Product> findAllByDeletedTrue();

    List<Product> findAllByStatusAndDeletedFalse(
            Status status
    );

    boolean existsByProductCode(String productCode);

    boolean existsByProductNameIgnoreCaseAndDeletedFalse(
            String productName
    );

    boolean existsByProductNameIgnoreCaseAndIdNotAndDeletedFalse(
            String productName,
            Long id
    );

    long countByDeletedFalse();

    long countByStatusAndDeletedFalse(Status status);


    // ==========================================
    // PESSIMISTIC LOCK
    // ==========================================

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT p
        FROM Product p
        WHERE p.productCode = :code
          AND p.deleted = false
    """)
    Optional<Product> findByCodeForUpdate(
            @Param("code") String code
    );

}