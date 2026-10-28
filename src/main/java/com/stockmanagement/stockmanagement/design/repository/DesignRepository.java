package com.stockmanagement.stockmanagement.design.repository;


import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.design.entity.Design;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DesignRepository
        extends JpaRepository<Design, Long> {

    Optional<Design> findByDesignCodeAndDeletedFalse(
            String designCode
    );

    Optional<Design> findByDesignCodeAndDeletedTrue(
            String designCode
    );

    List<Design> findAllByDeletedFalse();

    List<Design> findAllByDeletedTrue();

    List<Design> findAllByStatusAndDeletedFalse(
            Status status
    );

    boolean existsByDesignCode(String designCode);

    boolean existsByDesignNameIgnoreCaseAndDeletedFalse(
            String designName
    );

    boolean existsByDesignNameIgnoreCaseAndIdNotAndDeletedFalse(
            String designName,
            Long id
    );

    long countByDeletedFalse();

    long countByStatusAndDeletedFalse(Status status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT d
    FROM Design d
    WHERE d.designCode = :code
      AND d.deleted = false
""")
    Optional<Design> findByCodeForUpdate(
            @Param("code") String code
    );

}
