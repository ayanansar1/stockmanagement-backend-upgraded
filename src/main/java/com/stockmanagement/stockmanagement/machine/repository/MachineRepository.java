package com.stockmanagement.stockmanagement.machine.repository;



import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MachineRepository
        extends JpaRepository<Machine, Long> {

    Optional<Machine>
    findByMachineCodeAndDeletedFalse(
            String machineCode
    );

    Optional<Machine>
    findByMachineCodeAndDeletedTrue(
            String machineCode
    );

    List<Machine>
    findAllByDeletedFalse();

    List<Machine>
    findAllByDeletedTrue();

    List<Machine>
    findAllByKareegarAndDeletedFalse(
            Kareegar kareegar
    );

    List<Machine>
    findAllByKareegarAndDeletedTrue(
            Kareegar kareegar
    );

    boolean existsByMachineCode(
            String machineCode
    );

    boolean existsByKareegarAndMachineNo(
            Kareegar kareegar,
            Long machineNo
    );
    List<Machine> findAllByDeletedFalseAndMachineStatus(Status machineStatus);


    List<Machine> findAllByDeletedTrueAndMachineStatus(Status machineStatus);


    long countByDeletedFalse();

    long countByMachineStatusAndDeletedFalse(Status machineStatus);


}
