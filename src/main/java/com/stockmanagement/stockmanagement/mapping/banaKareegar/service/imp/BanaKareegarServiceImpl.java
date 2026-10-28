package com.stockmanagement.stockmanagement.mapping.banaKareegar.service.imp;

import com.stockmanagement.stockmanagement.bana.entity.Bana;
import com.stockmanagement.stockmanagement.bana.repository.BanaRepository;
import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.kareegar.repository.KareegarRepository;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.machine.repository.MachineRepository;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.dto.BanaKareegarCreateRequest;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.dto.BanaKareegarResponse;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.dto.BanaKareegarUpdateRequest;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.entity.BanaKareegar;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.mapper.BanaKareegarMapper;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.repository.BanaKareegarRepository;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.service.BanaKareegarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BanaKareegarServiceImpl
        implements BanaKareegarService {

    private final BanaKareegarRepository banaKareegarRepository;

    private final KareegarRepository kareegarRepository;

    private final BanaRepository banaRepository;

    private final MachineRepository machineRepository;

    private final BusinessCodeService businessCodeService;

    private final BanaKareegarMapper mapper;


    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public BanaKareegarResponse create(
            BanaKareegarCreateRequest request) {

        /*
         * Lock order:
         *
         * 1. Kareegar
         * 2. Bana
         */

        Kareegar kareegar =
                kareegarRepository.findActiveForUpdate(
                        request.kareegarCode(),
                        Status.ACTIVE
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Active Kareegar not found: "
                                        + request.kareegarCode()
                        )
                );

        Bana bana =
                banaRepository.findByCodeForUpdate(
                        request.banaCode()
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Active Bana not found: "
                                        + request.banaCode()
                        )
                );

        if (bana.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException(
                    "Bana is not active: "
                            + request.banaCode()
            );
        }

        Machine machine =
                machineRepository
                        .findByMachineCodeAndDeletedFalse(
                                request.machineCode()
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Machine not found: "
                                                + request.machineCode()
                                )
                        );

        validateMachine(
                machine,
                kareegar,
                request.machineCode()
        );

        /*
         * Business Rule:
         *
         * Same Bana can be used by multiple
         * Kareegars and Machines.
         *
         * But exact same:
         *
         * Kareegar + Bana + Machine
         *
         * cannot be duplicated.
         */
        boolean duplicate =
                banaKareegarRepository
                        .existsByKareegarAndBanaAndMachineAndDeletedFalse(
                                kareegar,
                                bana,
                                machine
                        );

        if (duplicate) {
            throw new IllegalStateException(
                    "This Bana is already assigned to this "
                            + "Kareegar and Machine"
            );
        }

        BanaKareegar entity =
                BanaKareegar.builder()
                        .banaKareegarCode(
                                businessCodeService.generateCode(
                                        CodeType.BANA_KAREEGAR
                                )
                        )
                        .kareegar(kareegar)
                        .bana(bana)
                        .machine(machine)
                        .status(Status.ACTIVE)
                        .assignedDate(
                                request.assignedDate()
                        )
                        .build();

        BanaKareegar saved =
                banaKareegarRepository.save(entity);

        return mapper.toResponse(saved);
    }


    // =========================================================
    // GET BY CODE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public BanaKareegarResponse getByCode(
            String code) {

        BanaKareegar entity =
                getNonDeleted(code);

        return mapper.toResponse(entity);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<BanaKareegarResponse> getAll() {

        return banaKareegarRepository
                .findAllByDeletedFalse()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET ACTIVE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<BanaKareegarResponse> getActive() {

        return banaKareegarRepository
                .findAllByStatusAndDeletedFalse(
                        Status.ACTIVE
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET DELETED
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<BanaKareegarResponse> getDeleted() {

        return banaKareegarRepository
                .findAllByDeletedTrue()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET BY KAREEGAR
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<BanaKareegarResponse> getByKareegar(
            String kareegarCode) {

        Kareegar kareegar =
                kareegarRepository
                        .findByKareegarCodeAndDeletedFalse(
                                kareegarCode
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Kareegar not found: "
                                                + kareegarCode
                                )
                        );

        return banaKareegarRepository
                .findAllByKareegarAndDeletedFalse(kareegar)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public BanaKareegarResponse update(
            String code,
            BanaKareegarUpdateRequest request) {

        BanaKareegar entity =
                getNonDeleted(code);

        /*
         * Lock order:
         *
         * Kareegar -> Bana
         */

        Kareegar newKareegar =
                kareegarRepository.findActiveForUpdate(
                        request.kareegarCode(),
                        Status.ACTIVE
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Active Kareegar not found: "
                                        + request.kareegarCode()
                        )
                );

        Bana newBana =
                banaRepository.findByCodeForUpdate(
                        request.banaCode()
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Bana not found: "
                                        + request.banaCode()
                        )
                );

        if (newBana.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException(
                    "Bana is not active: "
                            + request.banaCode()
            );
        }

        Machine newMachine =
                machineRepository
                        .findByMachineCodeAndDeletedFalse(
                                request.machineCode()
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Machine not found: "
                                                + request.machineCode()
                                )
                        );

        validateMachine(
                newMachine,
                newKareegar,
                request.machineCode()
        );

        /*
         * Same Bana can exist in multiple mappings.
         *
         * But exact same:
         *
         * Kareegar + Bana + Machine
         *
         * must not exist twice.
         *
         * Current record is excluded by ID.
         */
        boolean duplicate =
                banaKareegarRepository
                        .existsByKareegarAndBanaAndMachineAndDeletedFalseAndIdNot(
                                newKareegar,
                                newBana,
                                newMachine,
                                entity.getId()
                        );

        if (duplicate) {
            throw new IllegalStateException(
                    "This Bana is already assigned to this "
                            + "Kareegar and Machine"
            );
        }

        entity.setKareegar(newKareegar);
        entity.setBana(newBana);
        entity.setMachine(newMachine);
        entity.setAssignedDate(
                request.assignedDate()
        );

        BanaKareegar updated =
                banaKareegarRepository.save(entity);

        return mapper.toResponse(updated);
    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @Override
    public BanaKareegarResponse activate(
            String code) {

        BanaKareegar entity =
                getNonDeleted(code);

        if (entity.getStatus() == Status.ACTIVE) {
            return mapper.toResponse(entity);
        }

        Kareegar kareegar =
                kareegarRepository.findActiveForUpdate(
                        entity.getKareegar().getKareegarCode(),
                        Status.ACTIVE
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Kareegar is not active"
                        )
                );

        Bana bana =
                banaRepository.findByCodeForUpdate(
                        entity.getBana().getBanaCode()
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Bana not found"
                        )
                );

        if (bana.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException(
                    "Bana is not active"
            );
        }

        Machine machine =
                machineRepository
                        .findByMachineCodeAndDeletedFalse(
                                entity.getMachine().getMachineCode()
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Machine not found"
                                )
                        );

        validateMachine(
                machine,
                kareegar,
                machine.getMachineCode()
        );

        /*
         * Same Bana can have multiple ACTIVE mappings.
         *
         * But this exact:
         *
         * Kareegar + Bana + Machine
         *
         * should not already exist in another record.
         */
        boolean duplicate =
                banaKareegarRepository
                        .existsByKareegarAndBanaAndMachineAndDeletedFalseAndIdNot(
                                kareegar,
                                bana,
                                machine,
                                entity.getId()
                        );

        if (duplicate) {
            throw new IllegalStateException(
                    "This Bana is already assigned to this "
                            + "Kareegar and Machine"
            );
        }

        entity.setStatus(Status.ACTIVE);

        return mapper.toResponse(
                banaKareegarRepository.save(entity)
        );
    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Override
    public BanaKareegarResponse deactivate(
            String code) {

        BanaKareegar entity =
                getNonDeleted(code);

        if (entity.getStatus() == Status.INACTIVE) {
            return mapper.toResponse(entity);
        }

        entity.setStatus(Status.INACTIVE);

        return mapper.toResponse(
                banaKareegarRepository.save(entity)
        );
    }


    // =========================================================
    // SOFT DELETE
    // =========================================================

    @Override
    public void delete(String code) {

        BanaKareegar entity =
                getNonDeleted(code);

        entity.setStatus(Status.INACTIVE);

        entity.softDelete();

        banaKareegarRepository.save(entity);
    }


    // =========================================================
    // RESTORE
    // =========================================================

    @Override
    public BanaKareegarResponse restore(
            String code) {

        BanaKareegar entity =
                banaKareegarRepository
                        .findByBanaKareegarCodeAndDeletedTrue(
                                code
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Deleted BanaKareegar not found: "
                                                + code
                                )
                        );

        Kareegar kareegar =
                kareegarRepository.findActiveForUpdate(
                        entity.getKareegar().getKareegarCode(),
                        Status.ACTIVE
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Kareegar is not active"
                        )
                );

        Bana bana =
                banaRepository.findByCodeForUpdate(
                        entity.getBana().getBanaCode()
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Bana not found"
                        )
                );

        if (bana.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException(
                    "Bana is not active"
            );
        }

        Machine machine =
                machineRepository
                        .findByMachineCodeAndDeletedFalse(
                                entity.getMachine().getMachineCode()
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Machine not found"
                                )
                        );

        validateMachine(
                machine,
                kareegar,
                machine.getMachineCode()
        );

        /*
         * Restore is allowed when same Bana is already
         * assigned elsewhere.
         *
         * But exact same active mapping must not exist.
         */
        boolean duplicate =
                banaKareegarRepository
                        .existsByKareegarAndBanaAndMachineAndDeletedFalse(
                                kareegar,
                                bana,
                                machine
                        );

        if (duplicate) {
            throw new IllegalStateException(
                    "This Bana is already assigned to this "
                            + "Kareegar and Machine"
            );
        }

        entity.restore();
        entity.setStatus(Status.ACTIVE);

        return mapper.toResponse(
                banaKareegarRepository.save(entity)
        );
    }


    // =========================================================
    // PRIVATE HELPERS
    // =========================================================

    private BanaKareegar getNonDeleted(
            String code) {

        return banaKareegarRepository
                .findByBanaKareegarCodeAndDeletedFalse(code)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "BanaKareegar not found: "
                                        + code
                        )
                );
    }


    private void validateMachine(
            Machine machine,
            Kareegar kareegar,
            String machineCode) {

        if (machine == null) {
            throw new IllegalStateException(
                    "Machine not found: "
                            + machineCode
            );
        }

        if (machine.getMachineStatus() == null) {
            throw new IllegalStateException(
                    "Machine status is not configured: "
                            + machineCode
            );
        }

        if (machine.getMachineStatus()
                != Status.ACTIVE) {

            throw new IllegalStateException(
                    "Machine is not active: "
                            + machineCode
            );
        }

        if (machine.getKareegar() == null) {
            throw new IllegalStateException(
                    "Machine is not assigned to any Kareegar: "
                            + machineCode
            );
        }

        if (!machine.getKareegar()
                .getId()
                .equals(kareegar.getId())) {

            throw new IllegalStateException(
                    "Machine " + machineCode
                            + " belongs to Kareegar "
                            + machine.getKareegar().getKareegarCode()
                            + ", not "
                            + kareegar.getKareegarCode()
            );
        }
    }
}