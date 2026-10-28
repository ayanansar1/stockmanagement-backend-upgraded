package com.stockmanagement.stockmanagement.mapping.productKareegar.repository;

import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.mapping.productKareegar.entity.ProductKareegar;
import com.stockmanagement.stockmanagement.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductKareegarRepository
        extends JpaRepository<ProductKareegar, Long> {

    Optional<ProductKareegar>
    findByProductKareegarCodeAndDeletedFalse(
            String code
    );

    Optional<ProductKareegar>
    findByProductKareegarCodeAndDeletedTrue(
            String code
    );

    List<ProductKareegar>
    findAllByDeletedFalse();

    List<ProductKareegar>
    findAllByDeletedTrue();

    List<ProductKareegar>
    findAllByStatusAndDeletedFalse(
            Status status
    );

    List<ProductKareegar>
    findAllByKareegarAndDeletedFalse(
            Kareegar kareegar
    );

    boolean existsByKareegarAndProductAndMachineAndDeletedFalse(
            Kareegar kareegar,
            Product product,
            Machine machine
    );

    boolean existsByKareegarAndProductAndMachineAndDeletedFalseAndIdNot(
            Kareegar kareegar,
            Product product,
            Machine machine,
            Long id
    );

    Optional<ProductKareegar>
    findByProduct_ProductCodeAndKareegarAndMachineAndDeletedFalse(
            String productCode,
            Kareegar kareegar,
            Machine machine
    );



    boolean existsByProductKareegarCode(
            String code
    );
}