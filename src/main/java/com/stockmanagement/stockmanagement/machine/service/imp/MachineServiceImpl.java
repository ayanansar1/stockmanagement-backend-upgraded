package com.stockmanagement.stockmanagement.machine.service.imp;



import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.kareegar.repository.KareegarRepository;
import com.stockmanagement.stockmanagement.machine.dto.MachineCreateRequest;
import com.stockmanagement.stockmanagement.machine.dto.MachineResponse;
import com.stockmanagement.stockmanagement.machine.dto.MachineUpdateRequest;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.machine.mapper.MachineMapper;
import com.stockmanagement.stockmanagement.machine.repository.MachineRepository;
import com.stockmanagement.stockmanagement.machine.service.MachineNumberService;
import com.stockmanagement.stockmanagement.machine.service.MachineService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MachineServiceImpl
        implements MachineService {

    private final MachineRepository machineRepository;

    private final KareegarRepository kareegarRepository;

    private final MachineMapper machineMapper;

    private final MachineNumberService machineNumberService;

    private final BusinessCodeService businessCodeService;


    @Override
    public MachineResponse create(
            String kareegarCode,
            MachineCreateRequest request) {

        Kareegar kareegar =
                kareegarRepository
                        .findActiveForUpdate(
                                kareegarCode,
                                Status.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Active Kareegar not found: "
                                                + kareegarCode
                                )
                        );

        Machine machine =
                machineMapper.toEntity(request);

        Long machineNo =
                machineNumberService
                        .generateNextNumber(kareegar);

        String machineCode =
                businessCodeService
                        .generateCode(
                                CodeType.MACHINE
                        );

        machine.setMachineNo(machineNo);

        machine.setMachineCode(machineCode);

        machine.setKareegar(kareegar);

        machine.setMachineStatus(
                Status.ACTIVE
        );

        Machine saved =
                machineRepository.save(machine);

        return machineMapper.toResponse(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public MachineResponse getByCode(
            String machineCode) {

        Machine machine =
                findNotDeleted(machineCode);

        return machineMapper.toResponse(machine);
    }


    @Override
    @Transactional(readOnly = true)
    public List<MachineResponse> getByKareegar(
            String kareegarCode) {

        Kareegar kareegar =
                kareegarRepository
                        .findByKareegarCodeAndDeletedFalse(
                                kareegarCode
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Kareegar not found: "
                                                + kareegarCode
                                )
                        );

        return machineRepository
                .findAllByKareegarAndDeletedFalse(
                        kareegar
                )
                .stream()
                .map(machineMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<MachineResponse> getAll() {

        return machineRepository
                .findAllByDeletedFalse()
                .stream()
                .map(machineMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MachineResponse> getActive() {

        return machineRepository
                .findAllByDeletedFalseAndMachineStatus(Status.ACTIVE)
                .stream()
                .map(machineMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MachineResponse> getInactive() {

        return machineRepository
                .findAllByDeletedFalseAndMachineStatus(Status.INACTIVE)
                .stream()
                .map(machineMapper::toResponse)
                .toList();
    }


    @Override
    public MachineResponse update(
            String machineCode,
            MachineUpdateRequest request) {

        Machine machine =
                findNotDeleted(machineCode);

        machineMapper.updateEntity(
                request,
                machine
        );

        return machineMapper.toResponse(machine);
    }


    @Override
    public MachineResponse activate(
            String machineCode) {

        Machine machine =
                findNotDeleted(machineCode);

        Kareegar kareegar =
                machine.getKareegar();

        if (kareegar.isDeleted()
                || kareegar.getStatus()
                != Status.ACTIVE) {

            throw new IllegalStateException(
                    "Machine cannot be activated. "
                            + "Kareegar is inactive or deleted."
            );
        }

        machine.setMachineStatus(
                Status.ACTIVE
        );

        return machineMapper.toResponse(machine);
    }


    @Override
    public MachineResponse deactivate(
            String machineCode) {

        Machine machine =
                findNotDeleted(machineCode);

        machine.setMachineStatus(
                Status.INACTIVE
        );

        return machineMapper.toResponse(machine);
    }


    @Override
    public void softDelete(
            String machineCode) {

        Machine machine =
                findNotDeleted(machineCode);

        machine.softDelete();

        machine.setMachineStatus(
                Status.INACTIVE
        );
    }


    @Override
    public MachineResponse restore(
            String machineCode) {

        Machine machine =
                machineRepository
                        .findByMachineCodeAndDeletedTrue(
                                machineCode
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Deleted Machine not found: "
                                                + machineCode
                                )
                        );

        Kareegar kareegar =
                machine.getKareegar();

        if (kareegar.isDeleted()
                || kareegar.getStatus()
                != Status.ACTIVE) {

            throw new IllegalStateException(
                    "Machine cannot be restored. "
                            + "Kareegar is inactive or deleted."
            );
        }

        machine.restore();

        machine.setMachineStatus(
                Status.ACTIVE
        );

        return machineMapper.toResponse(machine);
    }


    @Override
    @Transactional(readOnly = true)
    public List<MachineResponse> getDeleted() {

        return machineRepository
                .findAllByDeletedTrue()
                .stream()
                .map(machineMapper::toResponse)
                .toList();
    }


    private Machine findNotDeleted(
            String machineCode) {

        return machineRepository
                .findByMachineCodeAndDeletedFalse(
                        machineCode
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Machine not found: "
                                        + machineCode
                        )
                );
    }





}
