package com.stockmanagement.stockmanagement.mapping.designKareegar.repository;

import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.design.entity.Design;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.mapping.designKareegar.entity.DesignKareegar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DesignKareegarRepository
        extends JpaRepository<DesignKareegar, Long> {

    Optional<DesignKareegar>
    findByDesignKareegarCodeAndDeletedFalse(
            String code
    );

    Optional<DesignKareegar>
    findByDesignKareegarCodeAndDeletedTrue(
            String code
    );

    List<DesignKareegar>
    findAllByDeletedFalse();

    List<DesignKareegar>
    findAllByDeletedTrue();

    List<DesignKareegar>
    findAllByStatusAndDeletedFalse(
            Status status
    );

    List<DesignKareegar>
    findAllByKareegarAndDeletedFalse(
            Kareegar kareegar
    );

    /*
     * Same Design can be assigned to multiple
     * Kareegars and Machines.
     *
     * But exact same:
     *
     * Kareegar + Design + Machine
     *
     * cannot exist twice.
     */
    boolean existsByKareegarAndDesignAndMachineAndDeletedFalse(
            Kareegar kareegar,
            Design design,
            Machine machine
    );

    /*
     * Used during UPDATE.
     * Current record is excluded using ID.
     */
    boolean existsByKareegarAndDesignAndMachineAndDeletedFalseAndIdNot(
            Kareegar kareegar,
            Design design,
            Machine machine,
            Long id
    );

    Optional<DesignKareegar>
    findByDesign_DesignCodeAndKareegarAndMachineAndDeletedFalse(
            String designCode,
            Kareegar kareegar,
            Machine machine
    );

    boolean existsByDesignKareegarCode(
            String code
    );
}