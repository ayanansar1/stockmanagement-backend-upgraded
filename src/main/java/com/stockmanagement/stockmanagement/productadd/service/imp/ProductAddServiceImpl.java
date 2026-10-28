package com.stockmanagement.stockmanagement.productadd.service.imp;

import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;

import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.kareegar.repository.KareegarRepository;

import com.stockmanagement.stockmanagement.machine.entity.Machine;

import com.stockmanagement.stockmanagement.machine.repository.MachineRepository;

import com.stockmanagement.stockmanagement.mapping.banaKareegar.entity.BanaKareegar;
import com.stockmanagement.stockmanagement.mapping.banaKareegar.repository.BanaKareegarRepository;

import com.stockmanagement.stockmanagement.mapping.designKareegar.entity.DesignKareegar;
import com.stockmanagement.stockmanagement.mapping.designKareegar.repository.DesignKareegarRepository;

import com.stockmanagement.stockmanagement.mapping.productKareegar.entity.ProductKareegar;
import com.stockmanagement.stockmanagement.mapping.productKareegar.repository.ProductKareegarRepository;

import com.stockmanagement.stockmanagement.productadd.dto.ProductAddCreateRequest;
import com.stockmanagement.stockmanagement.productadd.dto.ProductAddResponse;
import com.stockmanagement.stockmanagement.productadd.entity.ProductAdd;
import com.stockmanagement.stockmanagement.productadd.mapper.ProductAddMapper;
import com.stockmanagement.stockmanagement.productadd.repository.ProductAddRepository;
import com.stockmanagement.stockmanagement.productadd.service.ProductAddService;

import com.stockmanagement.stockmanagement.stockbalance.service.ProductStockBalanceService;
import com.stockmanagement.stockmanagement.stockmovement.service.StockMovementService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAddServiceImpl
        implements ProductAddService {

    private final ProductAddRepository productAddRepository;

    private final KareegarRepository kareegarRepository;

    private final MachineRepository machineRepository;

    private final ProductKareegarRepository productKareegarRepository;

    private final BanaKareegarRepository banaKareegarRepository;

    private final DesignKareegarRepository designKareegarRepository;

    private final BusinessCodeService businessCodeService;

    private final ProductAddMapper productAddMapper;

    private final StockMovementService stockMovementService;

    private final ProductStockBalanceService productStockBalanceService;


    @Override
    @Transactional
    public ProductAddResponse create(
            ProductAddCreateRequest request) {

        /*
         * 1. Active Kareegar
         */
        Kareegar kareegar =
                kareegarRepository
                        .findActiveForUpdate(
                                request.kareegarCode(),
                                Status.ACTIVE
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Active Kareegar not found: "
                                                + request.kareegarCode()
                                )
                        );


        /*
         * 2. Machine
         */
        Machine machine =
                machineRepository
                        .findByMachineCodeAndDeletedFalse(
                                request.machineCode()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
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
         * 3. ProductKareegar
         *
         * productCode se mapping find hogi.
         * Kareegar + Machine bhi same hone chahiye.
         */
        ProductKareegar productKareegar =
                productKareegarRepository
                        .findByProduct_ProductCodeAndKareegarAndMachineAndDeletedFalse(
                                request.productCode(),
                                kareegar,
                                machine
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Active Product mapping not found for Product "
                                                + request.productCode()
                                                + ", Kareegar "
                                                + request.kareegarCode()
                                                + " and Machine "
                                                + request.machineCode()
                                )
                        );

        validateProductKareegar(
                productKareegar,
                request.productCode()
        );


        /*
         * 4. BanaKareegar
         *
         * banaCode se mapping find hogi.
         */
        BanaKareegar banaKareegar =
                banaKareegarRepository
                        .findByBana_BanaCodeAndKareegarAndMachineAndDeletedFalse(
                                request.banaCode(),
                                kareegar,
                                machine
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Active Bana mapping not found for Bana "
                                                + request.banaCode()
                                                + ", Kareegar "
                                                + request.kareegarCode()
                                                + " and Machine "
                                                + request.machineCode()
                                )
                        );

        validateBanaKareegar(
                banaKareegar,
                request.banaCode()
        );


        /*
         * 5. DesignKareegar
         *
         * designCode se mapping find hogi.
         */
        DesignKareegar designKareegar =
                designKareegarRepository
                        .findByDesign_DesignCodeAndKareegarAndMachineAndDeletedFalse(
                                request.designCode(),
                                kareegar,
                                machine
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Active Design mapping not found for Design "
                                                + request.designCode()
                                                + ", Kareegar "
                                                + request.kareegarCode()
                                                + " and Machine "
                                                + request.machineCode()
                                )
                        );

        validateDesignKareegar(
                designKareegar,
                request.designCode()
        );


        /*
         * 6. Generate ProductAdd code
         */
        String productAddCode =
                businessCodeService.generateCode(
                        CodeType.PRODUCT_ADD
                );


        /*
         * 7. Create ProductAdd
         */
        ProductAdd productAdd =
                ProductAdd.builder()
                        .productAddCode(productAddCode)
                        .kareegar(kareegar)
                        .machine(machine)
                        .productKareegar(productKareegar)
                        .banaKareegar(banaKareegar)
                        .designKareegar(designKareegar)
                        .quantity(request.quantity())
                        .unit(request.unit())
                        .addedDate(request.addedDate())
                        .status(Status.ACTIVE)
                        .build();


        ProductAdd saved =
                productAddRepository.save(productAdd);


        /*
         * 8. Ledger entry
         */
        stockMovementService.createInMovement(
                saved
        );


        /*
         * 9. Current stock update
         */
        productStockBalanceService.addStock(
                saved
        );


        /*
         * 10. Response
         */
        return productAddMapper.toResponse(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public ProductAddResponse getByCode(
            String productAddCode) {

        ProductAdd productAdd =
                productAddRepository
                        .findByProductAddCode(productAddCode)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product Add not found: "
                                                + productAddCode
                                )
                        );

        return productAddMapper.toResponse(productAdd);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductAddResponse> getAll() {

        return productAddRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(productAddMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductAddResponse> getByKareegar(
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

        return productAddRepository
                .findAllByKareegarOrderByCreatedAtDesc(
                        kareegar
                )
                .stream()
                .map(productAddMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductAddResponse> getByMachine(
            String machineCode) {

        Machine machine =
                machineRepository
                        .findByMachineCodeAndDeletedFalse(
                                machineCode
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Machine not found: "
                                                + machineCode
                                )
                        );

        return productAddRepository
                .findAllByMachineOrderByCreatedAtDesc(
                        machine
                )
                .stream()
                .map(productAddMapper::toResponse)
                .toList();
    }


    /*
     * =========================
     * MACHINE VALIDATION
     * =========================
     */
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
                            + machine.getKareegar().getKareegarCode()
                            + ", not "
                            + kareegar.getKareegarCode()
            );
        }
    }


    /*
     * =========================
     * PRODUCT VALIDATION
     * =========================
     */
    private void validateProductKareegar(
            ProductKareegar mapping,
            String productCode) {

        if (mapping.getStatus() == null
                || mapping.getStatus() != Status.ACTIVE) {

            throw new IllegalStateException(
                    "Product Kareegar mapping is not active: "
                            + productCode
            );
        }

        if (mapping.getProduct() == null) {
            throw new IllegalStateException(
                    "Product Kareegar mapping has no Product: "
                            + productCode
            );
        }

        if (mapping.getProduct().getStatus() == null
                || mapping.getProduct().getStatus() != Status.ACTIVE) {

            throw new IllegalStateException(
                    "Product is not active: "
                            + productCode
            );
        }
    }


    /*
     * =========================
     * BANA VALIDATION
     * =========================
     */
    private void validateBanaKareegar(
            BanaKareegar mapping,
            String banaCode) {

        if (mapping.getStatus() == null
                || mapping.getStatus() != Status.ACTIVE) {

            throw new IllegalStateException(
                    "Bana Kareegar mapping is not active: "
                            + banaCode
            );
        }

        if (mapping.getBana() == null) {
            throw new IllegalStateException(
                    "Bana Kareegar mapping has no Bana: "
                            + banaCode
            );
        }

        if (mapping.getBana().getStatus() == null
                || mapping.getBana().getStatus() != Status.ACTIVE) {

            throw new IllegalStateException(
                    "Bana is not active: "
                            + banaCode
            );
        }
    }


    /*
     * =========================
     * DESIGN VALIDATION
     * =========================
     */
    private void validateDesignKareegar(
            DesignKareegar mapping,
            String designCode) {

        if (mapping.getStatus() == null
                || mapping.getStatus() != Status.ACTIVE) {

            throw new IllegalStateException(
                    "Design Kareegar mapping is not active: "
                            + designCode
            );
        }

        if (mapping.getDesign() == null) {
            throw new IllegalStateException(
                    "Design Kareegar mapping has no Design: "
                            + designCode
            );
        }

        if (mapping.getDesign().getStatus() == null
                || mapping.getDesign().getStatus() != Status.ACTIVE) {

            throw new IllegalStateException(
                    "Design is not active: "
                            + designCode
            );
        }
    }
}