package com.stockmanagement.stockmanagement.bana.repository;



import com.stockmanagement.stockmanagement.bana.entity.Bana;
import com.stockmanagement.stockmanagement.common.enums.Status;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BanaRepository extends JpaRepository<Bana, Long> {

    Optional<Bana> findByBanaCodeAndDeletedFalse(String banaCode);

    Optional<Bana> findByBanaCodeAndDeletedTrue(String banaCode);

    List<Bana> findAllByDeletedFalse();

    List<Bana> findAllByDeletedTrue();

    List<Bana> findAllByStatusAndDeletedFalse(Status status);

    boolean existsByBanaCode(String banaCode);

    boolean existsByBanaNameIgnoreCaseAndDeletedFalse(String banaName);

    boolean existsByBanaNameIgnoreCaseAndIdNotAndDeletedFalse(
            String banaName,
            Long id
    );
    long countByDeletedFalse();

    long countByStatusAndDeletedFalse(Status status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT b
    FROM Bana b
    WHERE b.banaCode = :code
      AND b.deleted = false
""")
    Optional<Bana> findByCodeForUpdate(
            @Param("code") String code
    );

}