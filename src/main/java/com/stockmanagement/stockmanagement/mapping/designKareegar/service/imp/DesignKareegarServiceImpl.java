package com.stockmanagement.stockmanagement.mapping.designKareegar.service.imp;

import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
import com.stockmanagement.stockmanagement.design.entity.Design;
import com.stockmanagement.stockmanagement.design.repository.DesignRepository;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.kareegar.repository.KareegarRepository;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.machine.repository.MachineRepository;
import com.stockmanagement.stockmanagement.mapping.designKareegar.dto.DesignKareegarCreateRequest;
import com.stockmanagement.stockmanagement.mapping.designKareegar.dto.DesignKareegarResponse;
import com.stockmanagement.stockmanagement.mapping.designKareegar.dto.DesignKareegarUpdateRequest;
import com.stockmanagement.stockmanagement.mapping.designKareegar.entity.DesignKareegar;
import com.stockmanagement.stockmanagement.mapping.designKareegar.mapper.DesignKareegarMapper;
import com.stockmanagement.stockmanagement.mapping.designKareegar.repository.DesignKareegarRepository;
import com.stockmanagement.stockmanagement.mapping.designKareegar.service.DesignKareegarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DesignKareegarServiceImpl
        implements DesignKareegarService {

    private final DesignKareegarRepository
            designKareegarRepository;

    private final KareegarRepository
            kareegarRepository;

    private final DesignRepository
            designRepository;

    private final MachineRepository
            machineRepository;

    private final BusinessCodeService
            businessCodeService;

    private final DesignKareegarMapper
            mapper;


    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public DesignKareegarResponse create(
            DesignKareegarCreateRequest request) {

        /*
         * Lock order:
         *
         * 1. Kareegar
         * 2. Design
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

        Design design =
                designRepository.findByCodeForUpdate(
                        request.designCode()
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Design not found: "
                                        + request.designCode()
                        )
                );

        validateDesign(design);

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
         * Same Design can be used by multiple
         * Kareegars and Machines.
         *
         * But exact same:
         *
         * Kareegar + Design + Machine
         *
         * cannot be duplicated.
         */

        boolean duplicate =
                designKareegarRepository
                        .existsByKareegarAndDesignAndMachineAndDeletedFalse(
                                kareegar,
                                design,
                                machine
                        );

        if (duplicate) {
            throw new IllegalStateException(
                    "This Design is already assigned to this "
                            + "Kareegar and Machine"
            );
        }

        DesignKareegar entity =
                DesignKareegar.builder()
                        .designKareegarCode(
                                businessCodeService.generateCode(
                                        CodeType.DESIGN_KAREEGAR
                                )
                        )
                        .kareegar(kareegar)
                        .design(design)
                        .machine(machine)
                        .status(Status.ACTIVE)
                        .assignedDate(
                                request.assignedDate()
                        )
                        .build();

        DesignKareegar saved =
                designKareegarRepository.save(entity);

        return mapper.toResponse(saved);
    }


    // =========================================================
    // GET BY CODE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public DesignKareegarResponse getByCode(
            String code) {

        DesignKareegar entity =
                getNonDeleted(code);

        return mapper.toResponse(entity);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<DesignKareegarResponse> getAll() {

        return designKareegarRepository
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
    public List<DesignKareegarResponse> getActive() {

        return designKareegarRepository
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
    public List<DesignKareegarResponse> getDeleted() {

        return designKareegarRepository
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
    public List<DesignKareegarResponse> getByKareegar(
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

        return designKareegarRepository
                .findAllByKareegarAndDeletedFalse(kareegar)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public DesignKareegarResponse update(
            String code,
            DesignKareegarUpdateRequest request) {

        DesignKareegar entity =
                getNonDeleted(code);

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

        Design newDesign =
                designRepository.findByCodeForUpdate(
                        request.designCode()
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Design not found: "
                                        + request.designCode()
                        )
                );

        validateDesign(newDesign);

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
         * Same Design is allowed elsewhere.
         *
         * But exact same:
         *
         * Kareegar + Design + Machine
         *
         * cannot exist twice.
         *
         * Current record is excluded.
         */

        boolean duplicate =
                designKareegarRepository
                        .existsByKareegarAndDesignAndMachineAndDeletedFalseAndIdNot(
                                newKareegar,
                                newDesign,
                                newMachine,
                                entity.getId()
                        );

        if (duplicate) {
            throw new IllegalStateException(
                    "This Design is already assigned to this "
                            + "Kareegar and Machine"
            );
        }

        entity.setKareegar(newKareegar);
        entity.setDesign(newDesign);
        entity.setMachine(newMachine);
        entity.setAssignedDate(
                request.assignedDate()
        );

        DesignKareegar updated =
                designKareegarRepository.save(entity);

        return mapper.toResponse(updated);
    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @Override
    public DesignKareegarResponse activate(
            String code) {

        DesignKareegar entity =
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

        Design design =
                designRepository.findByCodeForUpdate(
                        entity.getDesign().getDesignCode()
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Design not found"
                        )
                );

        validateDesign(design);

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
         * Same Design can already be ACTIVE
         * somewhere else.
         *
         * That is allowed.
         *
         * Only exact same combination is blocked.
         */

        boolean duplicate =
                designKareegarRepository
                        .existsByKareegarAndDesignAndMachineAndDeletedFalseAndIdNot(
                                kareegar,
                                design,
                                machine,
                                entity.getId()
                        );

        if (duplicate) {
            throw new IllegalStateException(
                    "This Design is already assigned to this "
                            + "Kareegar and Machine"
            );
        }

        entity.setStatus(Status.ACTIVE);

        return mapper.toResponse(
                designKareegarRepository.save(entity)
        );
    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Override
    public DesignKareegarResponse deactivate(
            String code) {

        DesignKareegar entity =
                getNonDeleted(code);

        if (entity.getStatus() == Status.INACTIVE) {
            return mapper.toResponse(entity);
        }

        entity.setStatus(Status.INACTIVE);

        return mapper.toResponse(
                designKareegarRepository.save(entity)
        );
    }


    // =========================================================
    // DELETE
    // =========================================================

    @Override
    public void delete(String code) {

        DesignKareegar entity =
                getNonDeleted(code);

        entity.setStatus(Status.INACTIVE);

        entity.softDelete();

        designKareegarRepository.save(entity);
    }


    // =========================================================
    // RESTORE
    // =========================================================

    @Override
    public DesignKareegarResponse restore(
            String code) {

        DesignKareegar entity =
                designKareegarRepository
                        .findByDesignKareegarCodeAndDeletedTrue(
                                code
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Deleted DesignKareegar not found: "
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

        Design design =
                designRepository.findByCodeForUpdate(
                        entity.getDesign().getDesignCode()
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Design not found"
                        )
                );

        validateDesign(design);

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
         * Same Design can already be ACTIVE elsewhere.
         *
         * Only exact same combination is blocked.
         */

        boolean duplicate =
                designKareegarRepository
                        .existsByKareegarAndDesignAndMachineAndDeletedFalse(
                                kareegar,
                                design,
                                machine
                        );

        if (duplicate) {
            throw new IllegalStateException(
                    "This Design is already assigned to this "
                            + "Kareegar and Machine"
            );
        }

        entity.restore();
        entity.setStatus(Status.ACTIVE);

        return mapper.toResponse(
                designKareegarRepository.save(entity)
        );
    }


    // =========================================================
    // PRIVATE HELPERS
    // =========================================================

    private DesignKareegar getNonDeleted(
            String code) {

        return designKareegarRepository
                .findByDesignKareegarCodeAndDeletedFalse(code)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "DesignKareegar not found: "
                                        + code
                        )
                );
    }


    private void validateDesign(
            Design design) {

        if (design.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException(
                    "Design is not active: "
                            + design.getDesignCode()
            );
        }
    }


    private void validateMachine(
            Machine machine,
            Kareegar kareegar,
            String machineCode) {

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
                            + machine.getKareegar()
                            .getKareegarCode()
                            + ", not "
                            + kareegar.getKareegarCode()
            );
        }
    }
}