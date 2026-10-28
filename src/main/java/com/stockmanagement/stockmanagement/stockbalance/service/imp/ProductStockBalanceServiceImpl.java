package com.stockmanagement.stockmanagement.stockbalance.service.imp;

import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
import com.stockmanagement.stockmanagement.productadd.entity.ProductAdd;
import com.stockmanagement.stockmanagement.productreturn.entity.ProductReturn;
import com.stockmanagement.stockmanagement.productsell.entity.ProductSell;
import com.stockmanagement.stockmanagement.stockbalance.dto.ProductStockBalanceResponse;
import com.stockmanagement.stockmanagement.stockbalance.entity.ProductStockBalance;
import com.stockmanagement.stockmanagement.stockbalance.mapper.ProductStockBalanceMapper;
import com.stockmanagement.stockmanagement.stockbalance.repository.ProductStockBalanceRepository;
import com.stockmanagement.stockmanagement.stockbalance.service.ProductStockBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductStockBalanceServiceImpl
        implements ProductStockBalanceService {

    private final ProductStockBalanceRepository repository;
    private final ProductStockBalanceMapper mapper;
    private final BusinessCodeService businessCodeService;


    // =========================================================
    // ADD STOCK
    // =========================================================

    @Override
    @Transactional
    public ProductStockBalanceResponse addStock(ProductAdd productAdd) {

        if (productAdd == null) {
            throw new IllegalArgumentException(
                    "ProductAdd is required"
            );
        }

        ProductStockBalance balance =
                repository.findForUpdate(
                        productAdd.getKareegar(),
                        productAdd.getMachine(),
                        productAdd.getProductKareegar(),
                        productAdd.getBanaKareegar(),
                        productAdd.getDesignKareegar(),
                        productAdd.getUnit()
                ).orElse(null);


        // Existing balance
        if (balance != null) {

            balance.setQuantity(
                    balance.getQuantity()
                            .add(productAdd.getQuantity())
            );
        }


        // First stock for this combination
        else {

            String stockBalanceCode =
                    businessCodeService.generateCode(
                            CodeType.STOCK_BALANCE
                    );

            balance =
                    ProductStockBalance.builder()
                            .stockBalanceCode(stockBalanceCode)
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
                            .quantity(productAdd.getQuantity())
                            .unit(productAdd.getUnit())
                            .build();
        }

        ProductStockBalance saved =
                repository.save(balance);

        return mapper.toResponse(saved);
    }


    // =========================================================
    // DEDUCT STOCK
    // =========================================================

    @Override
    @Transactional
    public ProductStockBalanceResponse deductStock(
            ProductSell productSell) {

        if (productSell == null) {
            throw new IllegalArgumentException(
                    "ProductSell is required"
            );
        }

        ProductStockBalance balance =
                repository.findForUpdate(
                        productSell.getKareegar(),
                        productSell.getMachine(),
                        productSell.getProductKareegar(),
                        productSell.getBanaKareegar(),
                        productSell.getDesignKareegar(),
                        productSell.getUnit()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Stock balance not found for product sell"
                        )
                );


        BigDecimal availableQuantity =
                balance.getQuantity();

        BigDecimal requestedQuantity =
                productSell.getQuantity();




        // Insufficient stock
        if (availableQuantity.compareTo(requestedQuantity) < 0) {

            throw new IllegalStateException(
                    "Insufficient stock. Available: "
                            + availableQuantity
                            + ", Requested: "
                            + requestedQuantity
            );
        }


        // Deduct stock
        balance.setQuantity(
                availableQuantity.subtract(requestedQuantity)
        );


        ProductStockBalance saved =
                repository.save(balance);

        return mapper.toResponse(saved);
    }



// =========================================================
// ADD RETURNED STOCK
// PRODUCT RETURN → STOCK BALANCE INCREASE
// =========================================================

    @Override
    @Transactional
    public ProductStockBalanceResponse addReturnedStock(
            ProductReturn productReturn) {

        if (productReturn == null) {
            throw new IllegalArgumentException(
                    "ProductReturn is required"
            );
        }

        if (productReturn.getProductSell() == null) {
            throw new IllegalArgumentException(
                    "Original ProductSell is required"
            );
        }

        if (productReturn.getReturnQuantity() == null
                || productReturn.getReturnQuantity()
                .compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Return quantity must be greater than zero"
            );
        }

        ProductSell productSell = productReturn.getProductSell();

        ProductStockBalance balance =
                repository.findForUpdate(
                        productSell.getKareegar(),
                        productSell.getMachine(),
                        productSell.getProductKareegar(),
                        productSell.getBanaKareegar(),
                        productSell.getDesignKareegar(),
                        productSell.getUnit()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Stock balance not found for returned product"
                        )
                );

        // Return unit must match the stock balance unit.
        if (productReturn.getUnit() != balance.getUnit()) {
            throw new IllegalStateException(
                    "Return unit does not match stock balance unit"
            );
        }

        // Add returned quantity back to stock.
        balance.setQuantity(
                balance.getQuantity().add(
                        productReturn.getReturnQuantity()
                )
        );

        ProductStockBalance saved =
                repository.save(balance);

        return mapper.toResponse(saved);
    }


    // =========================================================
    // GET ALL
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductStockBalanceResponse> getAll() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    // =========================================================
    // GET BY CODE
    // =========================================================

    @Override
    @Transactional(readOnly = true)
    public ProductStockBalanceResponse getByCode(
            String stockBalanceCode) {

        ProductStockBalance balance =
                repository.findByStockBalanceCode(
                        stockBalanceCode
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Stock balance not found: "
                                        + stockBalanceCode
                        )
                );

        return mapper.toResponse(balance);
    }
}

//package com.stockmanagement.stockmanagement.stockbalance.service.imp;
//
//import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
//
//import com.stockmanagement.stockmanagement.common.generator.CodeType;
//import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
//import com.stockmanagement.stockmanagement.productadd.entity.ProductAdd;
//import com.stockmanagement.stockmanagement.stockbalance.dto.ProductStockBalanceResponse;
//import com.stockmanagement.stockmanagement.stockbalance.entity.ProductStockBalance;
//import com.stockmanagement.stockmanagement.stockbalance.mapper.ProductStockBalanceMapper;
//import com.stockmanagement.stockmanagement.stockbalance.repository.ProductStockBalanceRepository;
//import com.stockmanagement.stockmanagement.stockbalance.service.ProductStockBalanceService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ProductStockBalanceServiceImpl
//        implements ProductStockBalanceService {
//
//    private final ProductStockBalanceRepository repository;
//    private final ProductStockBalanceMapper mapper;
//
//    private final BusinessCodeService businessCodeService;
//
//
//    @Override
//    @Transactional
//    public ProductStockBalanceResponse addStock(
//            ProductAdd productAdd) {
//
//        ProductStockBalance balance =
//                repository.findForUpdate(
//                        productAdd.getKareegar(),
//                        productAdd.getMachine(),
//                        productAdd.getProductKareegar(),
//                        productAdd.getBanaKareegar(),
//                        productAdd.getDesignKareegar(),
//                        productAdd.getUnit()
//                ).orElse(null);
//
//
//        /*
//         * Existing balance
//         */
//        if (balance != null) {
//
//            balance.setQuantity(
//                    balance.getQuantity()
//                            .add(productAdd.getQuantity())
//            );
//        }
//
//
//        /*
//         * First stock for this combination
//         */
//        else {
//
//            String stockBalanceCode =
//                    businessCodeService.generateCode(
//                            CodeType.STOCK_BALANCE
//                    );
//
//            balance =
//                    ProductStockBalance.builder()
//
//                            .stockBalanceCode(
//                                    stockBalanceCode
//                            )
//
//                            .kareegar(
//                                    productAdd.getKareegar()
//                            )
//
//                            .machine(
//                                    productAdd.getMachine()
//                            )
//
//                            .productKareegar(
//                                    productAdd.getProductKareegar()
//                            )
//
//                            .banaKareegar(
//                                    productAdd.getBanaKareegar()
//                            )
//
//                            .designKareegar(
//                                    productAdd.getDesignKareegar()
//                            )
//
//                            .quantity(
//                                    productAdd.getQuantity()
//                            )
//
//                            .unit(
//                                    productAdd.getUnit()
//                            )
//
//                            .build();
//        }
//
//
//        ProductStockBalance saved =
//                repository.save(balance);
//
//        return mapper.toResponse(saved);
//    }
//
//
//    @Override
//    @Transactional(readOnly = true)
//    public List<ProductStockBalanceResponse> getAll() {
//
//        return repository.findAll()
//                .stream()
//                .map(mapper::toResponse)
//                .toList();
//    }
//
//
//    @Override
//    @Transactional(readOnly = true)
//    public ProductStockBalanceResponse getByCode(
//            String stockBalanceCode) {
//
//        ProductStockBalance balance =
//                repository.findByStockBalanceCode(stockBalanceCode)
//                        .orElseThrow(() ->
//                                new ResourceNotFoundException(
//                                        "Stock balance not found: "
//                                                + stockBalanceCode
//                                )
//                        );
//
//        return mapper.toResponse(balance);
//    }
//}
//
//
////package com.stockmanagement.stockmanagement.stockbalance.service.imp;
////
////
////import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
////import com.stockmanagement.stockmanagement.productadd.entity.ProductAdd;
////
////
////import com.stockmanagement.stockmanagement.stockbalance.dto.ProductStockBalanceResponse;
////import com.stockmanagement.stockmanagement.stockbalance.entity.ProductStockBalance;
////import com.stockmanagement.stockmanagement.stockbalance.mapper.ProductStockBalanceMapper;
////import com.stockmanagement.stockmanagement.stockbalance.repository.ProductStockBalanceRepository;
////import com.stockmanagement.stockmanagement.stockbalance.service.ProductStockBalanceService;
////import lombok.RequiredArgsConstructor;
////
////import org.springframework.stereotype.Service;
////
////import java.math.BigDecimal;
////import java.util.List;
////
////@Service
////@RequiredArgsConstructor
////public class ProductStockBalanceServiceImpl
////        implements ProductStockBalanceService {
////
////    private final ProductStockBalanceRepository repository;
////
////    private final ProductStockBalanceMapper mapper;
////
////
////    @Override
////    public ProductStockBalanceResponse addStock(
////            ProductAdd productAdd) {
////
////        ProductStockBalance balance =
////                repository.findForUpdate(
////
////                        productAdd.getKareegar(),
////
////                        productAdd.getMachine(),
////
////                        productAdd.getProductKareegar(),
////
////                        productAdd.getBanaKareegar(),
////
////                        productAdd.getDesignKareegar(),
////
////                        productAdd.getUnit()
////
////                ).orElse(null);
////
////
////        /*
////         * Existing balance
////         */
////        if (balance != null) {
////
////            balance.setQuantity(
////                    balance.getQuantity()
////                            .add(productAdd.getQuantity())
////            );
////
////        }
////
////        /*
////         * First stock for this combination
////         */
////        else {
////
////            balance =
////                    ProductStockBalance.builder()
////
////                            .kareegar(
////                                    productAdd.getKareegar()
////                            )
////
////                            .machine(
////                                    productAdd.getMachine()
////                            )
////
////                            .productKareegar(
////                                    productAdd.getProductKareegar()
////                            )
////
////                            .banaKareegar(
////                                    productAdd.getBanaKareegar()
////                            )
////
////                            .designKareegar(
////                                    productAdd.getDesignKareegar()
////                            )
////
////                            .quantity(
////                                    productAdd.getQuantity()
////                            )
////
////                            .unit(
////                                    productAdd.getUnit()
////                            )
////
////                            .build();
////        }
////
////
////        ProductStockBalance saved =
////                repository.save(balance);
////
////        return mapper.toResponse(saved);
////    }
////
////
////    @Override
////    public List<ProductStockBalanceResponse> getAll() {
////
////        return repository.findAll()
////                .stream()
////                .map(mapper::toResponse)
////                .toList();
////    }
////
////
////    @Override
////    public ProductStockBalanceResponse getByCode(
////            String stockBalanceCode) {
////
////        ProductStockBalance balance =
////                repository.findByStockBalanceCode(stockBalanceCode)
////                        .orElseThrow(() ->
////                                new ResourceNotFoundException(
////                                        "Stock balance not found: "
////                                                + stockBalanceCode
////                                )
////                        );
////
////        return mapper.toResponse(balance);
////    }
////}