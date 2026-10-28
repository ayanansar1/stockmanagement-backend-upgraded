package com.stockmanagement.stockmanagement.common.generator.repository;


import com.stockmanagement.stockmanagement.common.generator.CodeSequence;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CodeSequenceRepository
        extends JpaRepository<CodeSequence, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT c
            FROM CodeSequence c
            WHERE c.entityName = :entityName
            """)
    Optional<CodeSequence> findByEntityNameForUpdate(
            @Param("entityName") CodeType entityName
    );
}
