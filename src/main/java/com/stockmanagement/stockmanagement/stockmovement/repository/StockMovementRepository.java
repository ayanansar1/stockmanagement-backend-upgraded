package com.stockmanagement.stockmanagement.stockmovement.repository;

import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.stockmovement.entity.StockMovement;
import com.stockmanagement.stockmanagement.stockmovement.enums.StockMovementType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StockMovementRepository
        extends JpaRepository<StockMovement, Long> {

    Optional<StockMovement>
    findByMovementCode(String movementCode);

    List<StockMovement>
    findAllByOrderByMovementDateDescCreatedAtDesc();

    List<StockMovement>
    findAllByKareegarOrderByMovementDateDescCreatedAtDesc(
            Kareegar kareegar
    );

    List<StockMovement>
    findAllByMachineOrderByMovementDateDescCreatedAtDesc(
            Machine machine
    );

    List<StockMovement>
    findAllByMovementTypeOrderByMovementDateDescCreatedAtDesc(
            StockMovementType movementType
    );

    boolean existsByMovementCode(String movementCode);


    @Query("""
    SELECT sm.movementType, sm.unit, COALESCE(SUM(sm.quantity), 0)
    FROM StockMovement sm
    WHERE sm.deleted = false
      AND sm.movementDate >= :startDate
      AND sm.movementDate < :endDate
    GROUP BY sm.movementType, sm.unit
    ORDER BY sm.movementType, sm.unit
    """)
    List<Object[]> getMovementSummary(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<StockMovement>
    findTop10ByDeletedFalseOrderByMovementDateDescCreatedAtDesc();
}