package com.stockmanagement.stockmanagement.mapping.productKareegar.service.imp;

import com.stockmanagement.stockmanagement.common.enums.Role;
import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
import com.stockmanagement.stockmanagement.kareegar.repository.KareegarRepository;
import com.stockmanagement.stockmanagement.machine.entity.Machine;
import com.stockmanagement.stockmanagement.machine.repository.MachineRepository;
import com.stockmanagement.stockmanagement.mapping.productKareegar.dto.ProductKareegarCreateRequest;
import com.stockmanagement.stockmanagement.mapping.productKareegar.dto.ProductKareegarResponse;
import com.stockmanagement.stockmanagement.mapping.productKareegar.dto.ProductKareegarUpdateRequest;
import com.stockmanagement.stockmanagement.mapping.productKareegar.entity.ProductKareegar;
import com.stockmanagement.stockmanagement.mapping.productKareegar.mapper.ProductKareegarMapper;
import com.stockmanagement.stockmanagement.mapping.productKareegar.repository.ProductKareegarRepository;
import com.stockmanagement.stockmanagement.product.entity.Product;
import com.stockmanagement.stockmanagement.product.repository.ProductRepository;
import com.stockmanagement.stockmanagement.mapping.productKareegar.service.ProductKareegarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductKareegarServiceImpl
        implements ProductKareegarService {

    private final ProductKareegarRepository productKareegarRepository;

    private final KareegarRepository kareegarRepository;

    private final ProductRepository productRepository;

    private final MachineRepository machineRepository;

    private final BusinessCodeService businessCodeService;

    private final ProductKareegarMapper productKareegarMapper;


    // =========================================================
    // CREATE
    // =========================================================

    @Override
    public ProductKareegarResponse create(
            ProductKareegarCreateRequest request) {

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

        Product product =
                productRepository.findByCodeForUpdate(
                        request.productCode()
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Product not found: "
                                        + request.productCode()
                        )
                );

        validateProduct(product);

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

        validateDuplicate(
                kareegar,
                product,
                machine
        );

        String code =
                businessCodeService.generateCode(
                        CodeType.PRODUCT_KAREEGAR
                );

        ProductKareegar entity =
                ProductKareegar.builder()
                        .productKareegarCode(code)
                        .kareegar(kareegar)
                        .product(product)
                        .machine(machine)
                        .status(Status.ACTIVE)
                        .assignedDate(request.assignedDate())
                        .build();

        ProductKareegar saved =
                productKareegarRepository.save(entity);

        return productKareegarMapper.toResponse(saved);
    }


    // =========================================================
    // GET BY CODE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public ProductKareegarResponse getByCode(
            String productKareegarCode) {

        ProductKareegar entity =
                productKareegarRepository
                        .findByProductKareegarCodeAndDeletedFalse(
                                productKareegarCode
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "ProductKareegar not found: "
                                                + productKareegarCode
                                )
                        );

        return productKareegarMapper.toResponse(entity);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductKareegarResponse> getAll() {

        return productKareegarRepository
                .findAllByDeletedFalse()
                .stream()
                .map(productKareegarMapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET ACTIVE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductKareegarResponse> getActive() {

        return productKareegarRepository
                .findAllByStatusAndDeletedFalse(
                        Status.ACTIVE
                )
                .stream()
                .map(productKareegarMapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET DELETED
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductKareegarResponse> getDeleted() {

        return productKareegarRepository
                .findAllByDeletedTrue()
                .stream()
                .map(productKareegarMapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET BY KAREEGAR
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductKareegarResponse> getByKareegar(
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

        return productKareegarRepository
                .findAllByKareegarAndDeletedFalse(kareegar)
                .stream()
                .map(productKareegarMapper::toResponse)
                .toList();
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Override
    public ProductKareegarResponse update(
            String productKareegarCode,
            ProductKareegarUpdateRequest request) {

        ProductKareegar entity =
                productKareegarRepository
                        .findByProductKareegarCodeAndDeletedFalse(
                                productKareegarCode
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "ProductKareegar not found: "
                                                + productKareegarCode
                                )
                        );

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

        Product product =
                productRepository.findByCodeForUpdate(
                        request.productCode()
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Product not found: "
                                        + request.productCode()
                        )
                );

        validateProduct(product);

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

        boolean duplicate =
                productKareegarRepository
                        .existsByKareegarAndProductAndMachineAndDeletedFalseAndIdNot(
                                kareegar,
                                product,
                                machine,
                                entity.getId()
                        );

        if (duplicate) {
            throw new IllegalStateException(
                    "This Product is already assigned to this Kareegar and Machine"
            );
        }

        entity.setKareegar(kareegar);
        entity.setProduct(product);
        entity.setMachine(machine);
        entity.setAssignedDate(request.assignedDate());

        ProductKareegar updated =
                productKareegarRepository.save(entity);

        return productKareegarMapper.toResponse(updated);
    }


    // =========================================================
    // ACTIVATE
    // =========================================================

    @Override
    public ProductKareegarResponse activate(
            String productKareegarCode) {

        ProductKareegar entity =
                productKareegarRepository
                        .findByProductKareegarCodeAndDeletedFalse(
                                productKareegarCode
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "ProductKareegar not found: "
                                                + productKareegarCode
                                )
                        );

        if (entity.getStatus() == Status.ACTIVE) {
            return productKareegarMapper.toResponse(entity);
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

        Product product =
                productRepository.findByCodeForUpdate(
                        entity.getProduct().getProductCode()
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Product not found"
                        )
                );

        validateProduct(product);

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

        boolean duplicate =
                productKareegarRepository
                        .existsByKareegarAndProductAndMachineAndDeletedFalseAndIdNot(
                                kareegar,
                                product,
                                machine,
                                entity.getId()
                        );

        if (duplicate) {
            throw new IllegalStateException(
                    "This Product is already assigned to this Kareegar and Machine"
            );
        }

        entity.setKareegar(kareegar);
        entity.setProduct(product);
        entity.setMachine(machine);
        entity.setStatus(Status.ACTIVE);

        return productKareegarMapper.toResponse(
                productKareegarRepository.save(entity)
        );
    }


    // =========================================================
    // DEACTIVATE
    // =========================================================

    @Override
    public ProductKareegarResponse deactivate(
            String productKareegarCode) {

        ProductKareegar entity =
                productKareegarRepository
                        .findByProductKareegarCodeAndDeletedFalse(
                                productKareegarCode
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "ProductKareegar not found: "
                                                + productKareegarCode
                                )
                        );

        entity.setStatus(Status.INACTIVE);

        return productKareegarMapper.toResponse(
                productKareegarRepository.save(entity)
        );
    }


    // =========================================================
    // SOFT DELETE
    // =========================================================

    @Override
    public void delete(
            String productKareegarCode) {

        ProductKareegar entity =
                productKareegarRepository
                        .findByProductKareegarCodeAndDeletedFalse(
                                productKareegarCode
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "ProductKareegar not found: "
                                                + productKareegarCode
                                )
                        );

        entity.setStatus(Status.INACTIVE);
        entity.softDelete();

        productKareegarRepository.save(entity);
    }


    // =========================================================
    // RESTORE
    // =========================================================

    @Override
    public ProductKareegarResponse restore(
            String productKareegarCode) {

        ProductKareegar entity =
                productKareegarRepository
                        .findByProductKareegarCodeAndDeletedTrue(
                                productKareegarCode
                        )
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "Deleted ProductKareegar not found: "
                                                + productKareegarCode
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

        Product product =
                productRepository.findByCodeForUpdate(
                        entity.getProduct().getProductCode()
                ).orElseThrow(() ->
                        new IllegalStateException(
                                "Product not found"
                        )
                );

        validateProduct(product);

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

        boolean duplicate =
                productKareegarRepository
                        .existsByKareegarAndProductAndMachineAndDeletedFalse(
                                kareegar,
                                product,
                                machine
                        );

        if (duplicate) {
            throw new IllegalStateException(
                    "Another active ProductKareegar mapping already exists for this Kareegar, Product and Machine"
            );
        }

        entity.setKareegar(kareegar);
        entity.setProduct(product);
        entity.setMachine(machine);

        entity.restore();
        entity.setStatus(Status.ACTIVE);

        return productKareegarMapper.toResponse(
                productKareegarRepository.save(entity)
        );
    }


    // =========================================================
    // PRODUCT VALIDATION
    // =========================================================

    private void validateProduct(Product product) {

        if (product.getStatus() == null) {
            throw new IllegalStateException(
                    "Product status is not configured: "
                            + product.getProductCode()
            );
        }

        if (product.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException(
                    "Product is not active: "
                            + product.getProductCode()
            );
        }
    }


    // =========================================================
    // MACHINE VALIDATION
    // =========================================================

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


    // =========================================================
    // DUPLICATE VALIDATION
    // =========================================================

    private void validateDuplicate(
            Kareegar kareegar,
            Product product,
            Machine machine) {

        boolean duplicate =
                productKareegarRepository
                        .existsByKareegarAndProductAndMachineAndDeletedFalse(
                                kareegar,
                                product,
                                machine
                        );

        if (duplicate) {
            throw new IllegalStateException(
                    "This Product is already assigned to this Kareegar and Machine"
            );
        }
    }
}