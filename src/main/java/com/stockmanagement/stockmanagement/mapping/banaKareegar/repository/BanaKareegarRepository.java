package com.stockmanagement.stockmanagement.mapping.banaKareegar.repository;

import com.stockmanagement.stockmanagement.bana.entity.Bana;
import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.entity.BanaKareegar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BanaKareegarRepository
        extends JpaRepository<BanaKareegar, Long> {

    Optional<BanaKareegar>
    findByBanaKareegarCodeAndDeletedFalse(String code);

    Optional<BanaKareegar>
    findByBanaKareegarCodeAndDeletedTrue(String code);

    List<BanaKareegar>
    findAllByDeletedFalse();

    List<BanaKareegar>
    findAllByDeletedTrue();

    List<BanaKareegar>
    findAllByStatusAndDeletedFalse(Status status);

    List<BanaKareegar>
    findAllByKareegarAndDeletedFalse(Kareegar kareegar);

    /*
     * Same Bana can be assigned to multiple
     * Kareegars/Machines.
     *
     * But exact same:
     * Kareegar + Bana + Machine
     * cannot be active twice.
     */
    boolean existsByKareegarAndBanaAndMachineAndDeletedFalse(
            Kareegar kareegar,
            Bana bana,
            Machine machine
    );

    /*
     * Used during UPDATE.
     * Current record is excluded using id.
     */
    boolean existsByKareegarAndBanaAndMachineAndDeletedFalseAndIdNot(
            Kareegar kareegar,
            Bana bana,
            Machine machine,
            Long id
    );

    Optional<BanaKareegar>
    findByBana_BanaCodeAndKareegarAndMachineAndDeletedFalse(
            String banaCode,
            Kareegar kareegar,
            Machine machine
    );

    boolean existsByBanaKareegarCode(String code);
}