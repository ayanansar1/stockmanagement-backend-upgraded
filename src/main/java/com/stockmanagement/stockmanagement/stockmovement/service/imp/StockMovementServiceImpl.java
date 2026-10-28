package com.stockmanagement.stockmanagement.stockmovement.service.imp;

import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
import com.stockmanagement.stockmanagement.productadd.entity.ProductAdd;
import com.stockmanagement.stockmanagement.productreturn.entity.ProductReturn;
import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import com.stockmanagement.stockmanagement.stockmovement.dto.StockMovementResponse;
import com.stockmanagement.stockmanagement.stockmovement.entity.StockMovement;
import com.stockmanagement.stockmanagement.stockmovement.enums.StockMovementType;
import com.stockmanagement.stockmanagement.stockmovement.mapper.StockMovementMapper;
import com.stockmanagement.stockmanagement.stockmovement.repository.StockMovementRepository;
import com.stockmanagement.stockmanagement.stockmovement.service.StockMovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockMovementServiceImpl
        implements StockMovementService {

    private final StockMovementRepository repository;
    private final BusinessCodeService codeService;
    private final StockMovementMapper mapper;


    // =========================================================
    // PRODUCT ADD → IN
    // =========================================================

    @Override
    public StockMovementResponse createInMovement(
            ProductAdd productAdd
    ) {

        if (productAdd == null) {
            throw new IllegalArgumentException(
                    "ProductAdd is required"
            );
        }

        StockMovement movement =
                StockMovement.builder()
                        .movementCode(
                                codeService.generateCode(
                                        CodeType.STOCK_MOVEMENT
                                )
                        )
                        .productAdd(productAdd)
                        .kareegar(productAdd.getKareegar())
                        .machine(productAdd.getMachine())
                        .productKareegar(
                                productAdd.getProductKareegar()
                        )
                        .banaKareegar(
                                productAdd.getBanaKareegar()
                        )
                        .designKareegar(
                                productAdd.getDesignKareegar()
                        )
                        .movementType(
                                StockMovementType.IN
                        )
                        .quantity(productAdd.getQuantity())
                        .unit(productAdd.getUnit())
                        .movementDate(productAdd.getAddedDate())
                        .build();

        StockMovement saved =
                repository.save(movement);

        return mapper.toResponse(saved);
    }


    // =========================================================
    // PRODUCT SELL → OUT
    // =========================================================

    @Override
    public StockMovementResponse createOutMovement(
            ProductSell productSell
    ) {

        if (productSell == null) {
            throw new IllegalArgumentException(
                    "ProductSell is required"
            );
        }

        StockMovement movement =
                StockMovement.builder()
                        .movementCode(
                                codeService.generateCode(
                                        CodeType.STOCK_MOVEMENT
                                )
                        )
                        .productSell(productSell)
                        .kareegar(productSell.getKareegar())
                        .machine(productSell.getMachine())
                        .productKareegar(
                                productSell.getProductKareegar()
                        )
                        .banaKareegar(
                                productSell.getBanaKareegar()
                        )
                        .designKareegar(
                                productSell.getDesignKareegar()
                        )
                        .movementType(
                                StockMovementType.OUT
                        )
                        .quantity(productSell.getQuantity())
                        .unit(productSell.getUnit())
                        .movementDate(productSell.getSellDate())
                        .build();

        StockMovement saved =
                repository.save(movement);

        return mapper.toResponse(saved);
    }


// =========================================================
// PRODUCT RETURN → RETURN
// =========================================================

    @Override
    @Transactional
    public StockMovementResponse createReturnMovement(
            ProductReturn productReturn
    ) {

        if (productReturn == null) {
            throw new IllegalArgumentException(
                    "ProductReturn is required"
            );
        }

        if (productReturn.getProductSell() == null) {
            throw new IllegalArgumentException(
                    "Original ProductSell is required for return"
            );
        }

        ProductSell productSell = productReturn.getProductSell();

        StockMovement movement =
                StockMovement.builder()
                        .movementCode(
                                codeService.generateCode(
                                        CodeType.STOCK_MOVEMENT
                                )
                        )
                        .productReturn(productReturn)
                        .kareegar(productSell.getKareegar())
                        .machine(productSell.getMachine())
                        .productKareegar(
                                productSell.getProductKareegar()
                        )
                        .banaKareegar(
                                productSell.getBanaKareegar()
                        )
                        .designKareegar(
                                productSell.getDesignKareegar()
                        )
                        .movementType(
                                StockMovementType.RETURN
                        )
                        .quantity(
                                productReturn.getReturnQuantity()
                        )
                        .unit(productReturn.getUnit())
                        .movementDate(
                                productReturn.getReturnDate()
                        )
                        .build();

        StockMovement saved = repository.save(movement);

        return mapper.toResponse(saved);
    }





    // =========================================================
    // GET BY CODE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public StockMovementResponse getByCode(
            String movementCode
    ) {

        StockMovement movement =
                repository
                        .findByMovementCode(movementCode)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Stock movement not found: "
                                                + movementCode
                                )
                        );

        return mapper.toResponse(movement);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getAll() {

        return repository
                .findAllByOrderByMovementDateDescCreatedAtDesc()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET BY TYPE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<StockMovementResponse> getByType(
            StockMovementType movementType
    ) {

        return repository
                .findAllByMovementTypeOrderByMovementDateDescCreatedAtDesc(
                        movementType
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
//package com.stockmanagement.stockmanagement.stockmovement.service.imp;
//
//import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
//import com.stockmanagement.stockmanagement.common.generator.CodeType;
//import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
//import com.stockmanagement.stockmanagement.productadd.entity.ProductAdd;
//import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
//import com.stockmanagement.stockmanagement.stockmovement.dto.StockMovementResponse;
//import com.stockmanagement.stockmanagement.stockmovement.entity.StockMovement;
//import com.stockmanagement.stockmanagement.stockmovement.enums.StockMovementType;
//import com.stockmanagement.stockmanagement.stockmovement.mapper.StockMovementMapper;
//import com.stockmanagement.stockmanagement.stockmovement.repository.StockMovementRepository;
//import com.stockmanagement.stockmanagement.stockmovement.service.StockMovementService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class StockMovementServiceImpl
//        implements StockMovementService {
//
//    private final StockMovementRepository repository;
//    private final BusinessCodeService codeService;
//    private final StockMovementMapper mapper;
//
//
//    // =========================================================
//    // PRODUCT ADD → IN MOVEMENT
//    // =========================================================
//
//    @Override
//    public StockMovementResponse createInMovement(
//            ProductAdd productAdd) {
//
//        StockMovement movement =
//                StockMovement.builder()
//
//                        .movementCode(
//                                codeService.generateCode(
//                                        CodeType.STOCK_MOVEMENT
//                                )
//                        )
//
//                        .productAdd(productAdd)
//
//                        .kareegar(
//                                productAdd.getKareegar()
//                        )
//
//                        .machine(
//                                productAdd.getMachine()
//                        )
//
//                        .productKareegar(
//                                productAdd.getProductKareegar()
//                        )
//
//                        .banaKareegar(
//                                productAdd.getBanaKareegar()
//                        )
//
//                        .designKareegar(
//                                productAdd.getDesignKareegar()
//                        )
//
//                        .movementType(
//                                StockMovementType.IN
//                        )
//
//                        .quantity(
//                                productAdd.getQuantity()
//                        )
//
//                        .unit(
//                                productAdd.getUnit()
//                        )
//
//                        .movementDate(
//                                productAdd.getAddedDate()
//                        )
//
//                        .build();
//
//        StockMovement saved =
//                repository.save(movement);
//
//        return mapper.toResponse(saved);
//    }
//
//
//    // =========================================================
//    // PRODUCT SELL → OUT MOVEMENT
//    // =========================================================
//
//    @Override
//    public StockMovementResponse createOutMovement(
//            ProductSell productSell) {
//
//        StockMovement movement =
//                StockMovement.builder()
//
//                        .movementCode(
//                                codeService.generateCode(
//                                        CodeType.STOCK_MOVEMENT
//                                )
//                        )
//
//                        .productAdd(null)
//
//                        .kareegar(
//                                productSell.getKareegar()
//                        )
//
//                        .machine(
//                                productSell.getMachine()
//                        )
//
//                        .productKareegar(
//                                productSell.getProductKareegar()
//                        )
//
//                        .banaKareegar(
//                                productSell.getBanaKareegar()
//                        )
//
//                        .designKareegar(
//                                productSell.getDesignKareegar()
//                        )
//
//                        .movementType(
//                                StockMovementType.OUT
//                        )
//
//                        .quantity(
//                                productSell.getQuantity()
//                        )
//
//                        .unit(
//                                productSell.getUnit()
//                        )
//
//                        .movementDate(
//                                productSell.getSellDate()
//                        )
//
//                        .build();
//
//        StockMovement saved =
//                repository.save(movement);
//
//        return mapper.toResponse(saved);
//    }
//
//
//    // =========================================================
//    // GET BY CODE
//    // =========================================================
//
//    @Override
//    @Transactional(readOnly = true)
//    public StockMovementResponse getByCode(
//            String movementCode) {
//
//        StockMovement movement =
//                repository
//                        .findByMovementCode(movementCode)
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Stock movement not found: "
//                                                + movementCode
//                                )
//                        );
//
//        return mapper.toResponse(movement);
//    }
//
//
//    // =========================================================
//    // GET ALL
//    // =========================================================
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<StockMovementResponse> getAll() {
//
//        return repository
//                .findAllByOrderByMovementDateDescCreatedAtDesc()
//                .stream()
//                .map(mapper::toResponse)
//                .toList();
//    }
//
//
//    // =========================================================
//    // GET BY TYPE
//    // =========================================================
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<StockMovementResponse> getByType(
//            StockMovementType movementType) {
//
//        return repository
//                .findAllByMovementTypeOrderByMovementDateDescCreatedAtDesc(
//                        movementType
//                )
//                .stream()
//                .map(mapper::toResponse)
//                .toList();
//    }
//}