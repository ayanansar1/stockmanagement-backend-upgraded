package com.stockmanagement.stockmanagement.productadd.repository;

import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.productadd.entity.ProductAdd;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductAddRepository
        extends JpaRepository<ProductAdd, Long> {

    Optional<ProductAdd>
    findByProductAddCode(String productAddCode);

    List<ProductAdd>
    findAllByOrderByCreatedAtDesc();

    List<ProductAdd>
    findAllByKareegarOrderByCreatedAtDesc(
            Kareegar kareegar
    );

    List<ProductAdd>
    findAllByMachineOrderByCreatedAtDesc(
            Machine machine
    );

    boolean existsByProductAddCode(
            String productAddCode
    );
}