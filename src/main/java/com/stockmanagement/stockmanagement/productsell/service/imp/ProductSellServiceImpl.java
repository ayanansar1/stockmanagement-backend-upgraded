package com.stockmanagement.stockmanagement.productsell.service.imp;

import com.stockmanagement.stockmanagement.buyer.entity.Buyer;
import com.stockmanagement.stockmanagement.buyer.repository.BuyerRepository;
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
import com.stockmanagement.stockmanagement.productsell.dto.ProductSellResponse;
import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import com.stockmanagement.stockmanagement.productsell.mapper.ProductSellMapper;
import com.stockmanagement.stockmanagement.productsell.repository.ProductSellRepository;
import com.stockmanagement.stockmanagement.productsell.service.ProductSellService;
import com.stockmanagement.stockmanagement.salesinvoice.dto.AddInvoiceItemRequest;
import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSellServiceImpl
        implements ProductSellService {

    private final ProductSellRepository productSellRepository;
    private final ProductSellMapper productSellMapper;

    private final BusinessCodeService businessCodeService;

    private final KareegarRepository kareegarRepository;
    private final MachineRepository machineRepository;

    private final ProductKareegarRepository productKareegarRepository;
    private final BanaKareegarRepository banaKareegarRepository;
    private final DesignKareegarRepository designKareegarRepository;

    private final BuyerRepository buyerRepository;


    // =========================================================
    // CREATE PRODUCT SELL FOR INVOICE
    // =========================================================

    @Override
    public ProductSell createForInvoice(
            SalesInvoice invoice,
            AddInvoiceItemRequest request
    ) {

        if (invoice == null) {
            throw new IllegalArgumentException(
                    "Invoice is required"
            );
        }

        if (request == null) {
            throw new IllegalArgumentException(
                    "Invoice item request is required"
            );
        }


        // -----------------------------------------------------
        // 1. Buyer
        // -----------------------------------------------------

        Buyer buyer = invoice.getBuyer();

        if (buyer == null) {
            throw new IllegalStateException(
                    "Invoice buyer is required"
            );
        }

        if (buyer.getStatus() != Status.ACTIVE
                || buyer.isDeleted()) {

            throw new IllegalStateException(
                    "Buyer is not active: "
                            + buyer.getBuyerCode()
            );
        }


        // -----------------------------------------------------
        // 2. Kareegar
        // -----------------------------------------------------

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


        // -----------------------------------------------------
        // 3. Machine
        // -----------------------------------------------------

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
                kareegar
        );


        // -----------------------------------------------------
        // 4. Product-Kareegar Mapping
        // -----------------------------------------------------

        ProductKareegar productKareegar =
                productKareegarRepository
                        .findByProduct_ProductCodeAndKareegarAndMachineAndDeletedFalse(
                                request.productCode(),
                                kareegar,
                                machine
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product mapping not found for Product: "
                                                + request.productCode()
                                )
                        );

        validateProduct(productKareegar);


        // -----------------------------------------------------
        // 5. Bana-Kareegar Mapping
        // -----------------------------------------------------

        BanaKareegar banaKareegar =
                banaKareegarRepository
                        .findByBana_BanaCodeAndKareegarAndMachineAndDeletedFalse(
                                request.banaCode(),
                                kareegar,
                                machine
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Bana mapping not found for Bana: "
                                                + request.banaCode()
                                )
                        );

        validateBana(banaKareegar);


        // -----------------------------------------------------
        // 6. Design-Kareegar Mapping
        // -----------------------------------------------------

        DesignKareegar designKareegar =
                designKareegarRepository
                        .findByDesign_DesignCodeAndKareegarAndMachineAndDeletedFalse(
                                request.designCode(),
                                kareegar,
                                machine
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Design mapping not found for Design: "
                                                + request.designCode()
                                )
                        );

        validateDesign(designKareegar);


        // -----------------------------------------------------
        // 7. Create ProductSell
        // -----------------------------------------------------

        ProductSell productSell =
                ProductSell.builder()
                        .productSellCode(
                                businessCodeService.generateCode(
                                        CodeType.PRODUCT_SELL
                                )
                        )
                        .kareegar(kareegar)
                        .machine(machine)
                        .productKareegar(productKareegar)
                        .banaKareegar(banaKareegar)
                        .designKareegar(designKareegar)
                        .buyer(buyer)
                        .quantity(request.quantity())
                        .unit(request.unit())
                        .sellDate(request.sellDate())
                        .status(Status.ACTIVE)
                        .build();

        return productSellRepository.save(productSell);
    }


    // =========================================================
    // MACHINE VALIDATION
    // =========================================================

    private void validateMachine(
            Machine machine,
            Kareegar kareegar
    ) {

        if (machine.getMachineStatus() == null
                || machine.getMachineStatus()
                != Status.ACTIVE) {

            throw new IllegalStateException(
                    "Machine is not active: "
                            + machine.getMachineCode()
            );
        }

        if (machine.getKareegar() == null
                || machine.getKareegar().getId() == null
                || !machine.getKareegar()
                .getId()
                .equals(kareegar.getId())) {

            throw new IllegalStateException(
                    "Machine "
                            + machine.getMachineCode()
                            + " does not belong to Kareegar "
                            + kareegar.getKareegarCode()
            );
        }
    }


    // =========================================================
    // PRODUCT VALIDATION
    // =========================================================

    private void validateProduct(
            ProductKareegar mapping
    ) {

        if (mapping.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException(
                    "Product mapping is not active"
            );
        }

        if (mapping.getProduct() == null
                || mapping.getProduct().isDeleted()
                || mapping.getProduct().getStatus()
                != Status.ACTIVE) {

            throw new IllegalStateException(
                    "Product is not active"
            );
        }
    }


    // =========================================================
    // BANA VALIDATION
    // =========================================================

    private void validateBana(
            BanaKareegar mapping
    ) {

        if (mapping.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException(
                    "Bana mapping is not active"
            );
        }

        if (mapping.getBana() == null
                || mapping.getBana().isDeleted()
                || mapping.getBana().getStatus()
                != Status.ACTIVE) {

            throw new IllegalStateException(
                    "Bana is not active"
            );
        }
    }


    // =========================================================
    // DESIGN VALIDATION
    // =========================================================

    private void validateDesign(
            DesignKareegar mapping
    ) {

        if (mapping.getStatus() != Status.ACTIVE) {
            throw new IllegalStateException(
                    "Design mapping is not active"
            );
        }

        if (mapping.getDesign() == null
                || mapping.getDesign().isDeleted()
                || mapping.getDesign().getStatus()
                != Status.ACTIVE) {

            throw new IllegalStateException(
                    "Design is not active"
            );
        }
    }


    // =========================================================
    // GET BY CODE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public ProductSellResponse getByCode(
            String productSellCode
    ) {

        ProductSell entity =
                productSellRepository
                        .findByProductSellCode(
                                productSellCode
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Product sell not found: "
                                                + productSellCode
                                )
                        );

        return productSellMapper.toResponse(entity);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductSellResponse> getAll() {

        return productSellRepository
                .findAllByOrderBySellDateDescCreatedAtDesc()
                .stream()
                .map(productSellMapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET BY KAREEGAR
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductSellResponse> getByKareegar(
            String kareegarCode
    ) {

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

        return productSellRepository
                .findAllByKareegarOrderBySellDateDescCreatedAtDesc(
                        kareegar
                )
                .stream()
                .map(productSellMapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET BY MACHINE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductSellResponse> getByMachine(
            String machineCode
    ) {

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

        return productSellRepository
                .findAllByMachineOrderBySellDateDescCreatedAtDesc(
                        machine
                )
                .stream()
                .map(productSellMapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET BY BUYER
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductSellResponse> getByBuyer(
            String buyerCode
    ) {

        Buyer buyer =
                buyerRepository
                        .findByBuyerCodeAndDeletedFalse(
                                buyerCode
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Buyer not found: "
                                                + buyerCode
                                )
                        );

        return productSellRepository
                .findAllByBuyerOrderBySellDateDescCreatedAtDesc(
                        buyer
                )
                .stream()
                .map(productSellMapper::toResponse)
                .toList();
    }
}

//package com.stockmanagement.stockmanagement.productsell.service.imp;
//
//
//import com.stockmanagement.stockmanagement.buyer.entity.Buyer;
//import com.stockmanagement.stockmanagement.buyer.repository.BuyerRepository;
//import com.stockmanagement.stockmanagement.common.enums.Status;
//import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
//import com.stockmanagement.stockmanagement.common.generator.CodeType;
//import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
//import com.stockmanagement.stockmanagement.kareegar.entity.Kareegar;
//import com.stockmanagement.stockmanagement.kareegar.repository.KareegarRepository;
//import com.stockmanagement.stockmanagement.machine.entity.Machine;
//
//import com.stockmanagement.stockmanagement.machine.repository.MachineRepository;
//import com.stockmanagement.stockmanagement.mapping.banaKareegar.entity.BanaKareegar;
//import com.stockmanagement.stockmanagement.mapping.banaKareegar.repository.BanaKareegarRepository;
//import com.stockmanagement.stockmanagement.mapping.designKareegar.entity.DesignKareegar;
//import com.stockmanagement.stockmanagement.mapping.designKareegar.repository.DesignKareegarRepository;
//import com.stockmanagement.stockmanagement.mapping.productKareegar.entity.ProductKareegar;
//import com.stockmanagement.stockmanagement.mapping.productKareegar.repository.ProductKareegarRepository;
//import com.stockmanagement.stockmanagement.productsell.dto.ProductSellResponse;
//import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
//import com.stockmanagement.stockmanagement.productsell.mapper.ProductSellMapper;
//import com.stockmanagement.stockmanagement.productsell.repository.ProductSellRepository;
//import com.stockmanagement.stockmanagement.productsell.service.ProductSellService;
//import com.stockmanagement.stockmanagement.salesinvoice.dto.AddInvoiceItemRequest;
//import com.stockmanagement.stockmanagement.salesinvoice.entity.SalesInvoice;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ProductSellServiceImpl
//        implements ProductSellService {
//
//    private final ProductSellRepository productSellRepository;
//    private final ProductSellMapper productSellMapper;
//
//    private final BusinessCodeService businessCodeService;
//
//    private final KareegarRepository kareegarRepository;
//    private final MachineRepository machineRepository;
//    private final ProductKareegarRepository productKareegarRepository;
//    private final BanaKareegarRepository banaKareegarRepository;
//    private final DesignKareegarRepository designKareegarRepository;
//
//    @Override
//    @Transactional
//    public ProductSellResponse createForInvoice(
//            SalesInvoice invoice,
//            AddInvoiceItemRequest request
//    ) {
//
//        if (invoice == null) {
//            throw new IllegalArgumentException(
//                    "Invoice is required"
//            );
//        }
//
//        Kareegar kareegar =
//                kareegarRepository
//                        .findActiveForUpdate(
//                                request.kareegarCode(),
//                                Status.ACTIVE
//                        )
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Active Kareegar not found: "
//                                                + request.kareegarCode()
//                                )
//                        );
//
//        Machine machine =
//                machineRepository
//                        .findByMachineCodeAndDeletedFalse(
//                                request.machineCode()
//                        )
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Machine not found: "
//                                                + request.machineCode()
//                                )
//                        );
//
//        validateMachine(machine, kareegar);
//
//        ProductKareegar productKareegar =
//                productKareegarRepository
//                        .findByProduct_ProductCodeAndKareegarAndMachineAndDeletedFalse(
//                                request.productCode(),
//                                kareegar,
//                                machine
//                        )
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Active Product mapping not found"
//                                )
//                        );
//
//        validateProduct(productKareegar);
//
//        BanaKareegar banaKareegar =
//                banaKareegarRepository
//                        .findByBana_BanaCodeAndKareegarAndMachineAndDeletedFalse(
//                                request.banaCode(),
//                                kareegar,
//                                machine
//                        )
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Active Bana mapping not found"
//                                )
//                        );
//
//        validateBana(banaKareegar);
//
//        DesignKareegar designKareegar =
//                designKareegarRepository
//                        .findByDesign_DesignCodeAndKareegarAndMachineAndDeletedFalse(
//                                request.designCode(),
//                                kareegar,
//                                machine
//                        )
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Active Design mapping not found"
//                                )
//                        );
//
//        validateDesign(designKareegar);
//
//        ProductSell productSell =
//                ProductSell.builder()
//                        .productSellCode(
//                                businessCodeService.generateCode(
//                                        CodeType.PRODUCT_SELL
//                                )
//                        )
//                        .kareegar(kareegar)
//                        .machine(machine)
//                        .productKareegar(productKareegar)
//                        .banaKareegar(banaKareegar)
//                        .designKareegar(designKareegar)
//                        .buyer(invoice.getBuyer())
//                        .quantity(request.quantity())
//                        .unit(request.unit())
//                        .sellDate(request.sellDate())
//                        .status(Status.ACTIVE)
//                        .build();
//
//        ProductSell saved =
//                productSellRepository.save(productSell);
//
//        return productSellMapper.toResponse(saved);
//    }
//
//    private void validateMachine(
//            Machine machine,
//            Kareegar kareegar
//    ) {
//
//        if (machine.getMachineStatus() != Status.ACTIVE) {
//            throw new IllegalStateException(
//                    "Machine is not active: "
//                            + machine.getMachineCode()
//            );
//        }
//
//        if (machine.getKareegar() == null
//                || !machine.getKareegar()
//                .getId()
//                .equals(kareegar.getId())) {
//
//            throw new IllegalStateException(
//                    "Machine "
//                            + machine.getMachineCode()
//                            + " does not belong to Kareegar "
//                            + kareegar.getKareegarCode()
//            );
//        }
//    }
//
//    private void validateProduct(
//            ProductKareegar mapping
//    ) {
//
//        if (mapping.getStatus() != Status.ACTIVE) {
//            throw new IllegalStateException(
//                    "Product mapping is not active"
//            );
//        }
//
//        if (mapping.getProduct() == null
//                || mapping.getProduct().getStatus() != Status.ACTIVE) {
//
//            throw new IllegalStateException(
//                    "Product is not active"
//            );
//        }
//    }
//
//    private void validateBana(
//            BanaKareegar mapping
//    ) {
//
//        if (mapping.getStatus() != Status.ACTIVE) {
//            throw new IllegalStateException(
//                    "Bana mapping is not active"
//            );
//        }
//
//        if (mapping.getBana() == null
//                || mapping.getBana().getStatus() != Status.ACTIVE) {
//
//            throw new IllegalStateException(
//                    "Bana is not active"
//            );
//        }
//    }
//
//    private void validateDesign(
//            DesignKareegar mapping
//    ) {
//
//        if (mapping.getStatus() != Status.ACTIVE) {
//            throw new IllegalStateException(
//                    "Design mapping is not active"
//            );
//        }
//
//        if (mapping.getDesign() == null
//                || mapping.getDesign().getStatus() != Status.ACTIVE) {
//
//            throw new IllegalStateException(
//                    "Design is not active"
//            );
//        }
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public ProductSellResponse getByCode(
//            String productSellCode
//    ) {
//
//        ProductSell entity =
//                productSellRepository
//                        .findByProductSellCode(productSellCode)
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Product sell not found: "
//                                                + productSellCode
//                                )
//                        );
//
//        return productSellMapper.toResponse(entity);
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<ProductSellResponse> getAll() {
//
//        return productSellRepository
//                .findAllByOrderBySellDateDescCreatedAtDesc()
//                .stream()
//                .map(productSellMapper::toResponse)
//                .toList();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<ProductSellResponse> getByKareegar(
//            String kareegarCode
//    ) {
//
//        Kareegar kareegar =
//                kareegarRepository
//                        .findByKareegarCodeAndDeletedFalse(
//                                kareegarCode
//                        )
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Kareegar not found: "
//                                                + kareegarCode
//                                )
//                        );
//
//        return productSellRepository
//                .findAllByKareegarOrderBySellDateDescCreatedAtDesc(
//                        kareegar
//                )
//                .stream()
//                .map(productSellMapper::toResponse)
//                .toList();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<ProductSellResponse> getByMachine(
//            String machineCode
//    ) {
//
//        Machine machine =
//                machineRepository
//                        .findByMachineCodeAndDeletedFalse(
//                                machineCode
//                        )
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Machine not found: "
//                                                + machineCode
//                                )
//                        );
//
//        return productSellRepository
//                .findAllByMachineOrderBySellDateDescCreatedAtDesc(
//                        machine
//                )
//                .stream()
//                .map(productSellMapper::toResponse)
//                .toList();
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<ProductSellResponse> getByBuyer(
//            String buyerCode
//    ) {
//
//        Buyer buyer =
//                buyerRepository
//                        .findByBuyerCodeAndDeletedFalse(
//                                buyerCode
//                        )
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Buyer not found: "
//                                                + buyerCode
//                                )
//                        );
//
//        return productSellRepository
//                .findAllByBuyerOrderBySellDateDescCreatedAtDesc(
//                        buyer
//                )
//                .stream()
//                .map(productSellMapper::toResponse)
//                .toList();
//    }
//
//    private final BuyerRepository buyerRepository;
//}