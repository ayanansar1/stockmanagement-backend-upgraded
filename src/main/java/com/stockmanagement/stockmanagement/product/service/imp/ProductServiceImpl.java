package com.stockmanagement.stockmanagement.product.service.imp;



import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.exception.DuplicateResourceException;
import com.stockmanagement.stockmanagement.common.exception.ResourceNotFoundException;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
import com.stockmanagement.stockmanagement.product.dto.ProductCreateRequest;
import com.stockmanagement.stockmanagement.product.dto.ProductResponse;
import com.stockmanagement.stockmanagement.product.dto.ProductUpdateRequest;
import com.stockmanagement.stockmanagement.product.entity.Product;
import com.stockmanagement.stockmanagement.product.mapper.ProductMapper;
import com.stockmanagement.stockmanagement.product.repository.ProductRepository;
import com.stockmanagement.stockmanagement.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final BusinessCodeService businessCodeService;


    @Override
    public ProductResponse create(
            ProductCreateRequest request) {

        // Duplicate product name check
        if (productRepository
                .existsByProductNameIgnoreCaseAndDeletedFalse(
                        request.productName())) {

            throw new DuplicateResourceException(
                    "Product already exists: "
                            + request.productName()
            );
        }

        // DTO → Entity
        Product product =
                productMapper.toEntity(request);

        // Generate business code
        String productCode =
                businessCodeService.generateCode(
                        CodeType.PRODUCT
                );

        product.setProductCode(productCode);

        // New product is ACTIVE
        product.setStatus(Status.ACTIVE);

        // Default soft-delete state
        product.setDeleted(false);
        product.setDeletedAt(null);

        // Save
        Product savedProduct =
                productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }


    @Override
    @Transactional(readOnly = true)
    public ProductResponse getByCode(
            String productCode) {

        Product product =
                findNotDeleted(productCode);

        return productMapper.toResponse(product);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {

        return productRepository
                .findAllByDeletedFalse()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getActive() {

        return productRepository
                .findAllByStatusAndDeletedFalse(
                        Status.ACTIVE
                )
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getInactive() {

        return productRepository
                .findAllByStatusAndDeletedFalse(
                        Status.INACTIVE
                )
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }


    @Override
    public ProductResponse update(
            String productCode,
            ProductUpdateRequest request) {

        Product product =
                findNotDeleted(productCode);

        // Duplicate name check
        if (productRepository
                .existsByProductNameIgnoreCaseAndIdNotAndDeletedFalse(
                        request.productName(),
                        product.getId()
                )) {

            throw new DuplicateResourceException(
                    "Product already exists: "
                            + request.productName()
            );
        }

        productMapper.updateEntity(
                request,
                product
        );

        return productMapper.toResponse(product);
    }


    @Override
    public ProductResponse activate(
            String productCode) {

        Product product =
                findNotDeleted(productCode);

        product.setStatus(Status.ACTIVE);

        return productMapper.toResponse(product);
    }


    @Override
    public ProductResponse deactivate(
            String productCode) {

        Product product =
                findNotDeleted(productCode);

        product.setStatus(Status.INACTIVE);

        return productMapper.toResponse(product);
    }


    @Override
    public void softDelete(
            String productCode) {

        Product product =
                findNotDeleted(productCode);

        // Deleted product cannot remain ACTIVE
        product.setStatus(Status.INACTIVE);

        product.softDelete();
    }


    @Override
    public ProductResponse restore(
            String productCode) {

        Product product =
                productRepository
                        .findByProductCodeAndDeletedTrue(
                                productCode
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Deleted Product not found: "
                                                + productCode
                                )
                        );

        product.restore();

        // Restored product becomes ACTIVE
        product.setStatus(Status.ACTIVE);

        return productMapper.toResponse(product);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getDeleted() {

        return productRepository
                .findAllByDeletedTrue()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }


    private Product findNotDeleted(
            String productCode) {

        return productRepository
                .findByProductCodeAndDeletedFalse(
                        productCode
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Product not found: "
                                        + productCode
                        )
                );
    }
}