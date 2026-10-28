package com.stockmanagement.stockmanagement.buyer.repository;

import com.stockmanagement.stockmanagement.buyer.entity.Buyer;
import com.stockmanagement.stockmanagement.common.enums.Status;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BuyerRepository
        extends JpaRepository<Buyer, Long> {

    Optional<Buyer> findByBuyerCodeAndDeletedFalse(
            String buyerCode
    );

    Optional<Buyer> findByBuyerCodeAndDeletedTrue(
            String buyerCode
    );

    List<Buyer> findAllByDeletedFalse();

    List<Buyer> findAllByDeletedTrue();

    List<Buyer> findAllByStatusAndDeletedFalse(
            Status status
    );

    boolean existsByMobileNoAndDeletedFalse(
            String mobileNo
    );

    boolean existsByMobileNoAndDeletedFalseAndIdNot(
            String mobileNo,
            Long id
    );

    long countByDeletedFalse();

    long countByStatusAndDeletedFalse(Status status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT b
            FROM Buyer b
            WHERE b.buyerCode = :code
              AND b.deleted = false
            """)
    Optional<Buyer> findByCodeForUpdate(
            @Param("code") String code
    );
}