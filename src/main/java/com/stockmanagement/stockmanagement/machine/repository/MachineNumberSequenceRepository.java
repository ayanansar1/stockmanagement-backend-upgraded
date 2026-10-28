package com.stockmanagement.stockmanagement.machine.repository;


import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.MachineNumberSequence;
import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MachineNumberSequenceRepository
        extends JpaRepository<
        MachineNumberSequence,
        Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT s
        FROM MachineNumberSequence s
        WHERE s.kareegar = :kareegar
    """)
    Optional<MachineNumberSequence>
    findByKareegarForUpdate(
            @Param("kareegar")
            Kareegar kareegar
    );
}
