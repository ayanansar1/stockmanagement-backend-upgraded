package com.stockmanagement.stockmanagement.kareegar.repository;


import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KareegarRepository
        extends JpaRepository<Kareegar, Long> {

    List<Kareegar> findAllByDeletedFalse();

    List<Kareegar> findAllByDeletedTrue();

    Optional<Kareegar>
    findByKareegarCodeAndDeletedFalse(
            String kareegarCode
    );

    Optional<Kareegar>
    findByKareegarCodeAndDeletedTrue(
            String kareegarCode
    );

    boolean existsByMobileNo(
            String mobileNo
    );

    boolean existsByEmailId(
            String emailId
    );

    boolean existsByMobileNoAndIdNot(
            String mobileNo,
            Long id
    );

    boolean existsByEmailIdAndIdNot(
            String emailId,
            Long id
    );

    List<Kareegar> findAllByDeletedFalseAndStatus(Status status);

    List<Kareegar> findAllByDeletedTrueAndStatus(Status status);

    long countByDeletedFalse();
    long countByDeletedFalseAndStatus(Status status);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT k
    FROM Kareegar k
    WHERE k.kareegarCode = :code
      AND k.status = :status
      AND k.deleted = false
""")
    Optional<Kareegar> findActiveForUpdate(
            @Param("code") String code,
            @Param("status") Status status
    );
}