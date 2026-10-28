package com.stockmanagement.stockmanagement.productlisting.service.imp;


import com.stockmanagement.stockmanagement.common.enums.Status;
import com.stockmanagement.stockmanagement.common.generator.CodeType;
import com.stockmanagement.stockmanagement.common.generator.service.BusinessCodeService;
import com.stockmanagement.stockmanagement.product.entity.Product;
import com.stockmanagement.stockmanagement.product.repository.ProductRepository;
import com.stockmanagement.stockmanagement.productlisting.dto.*;
import com.stockmanagement.stockmanagement.productlisting.entity.*;
import com.stockmanagement.stockmanagement.productlisting.enums.ListingStatus;
import com.stockmanagement.stockmanagement.productlisting.enums.ListingType;
import com.stockmanagement.stockmanagement.productlisting.mapper.ProductListingMapper;
import com.stockmanagement.stockmanagement.productlisting.repository.*;
import com.stockmanagement.stockmanagement.productlisting.service.ProductListingService;
import com.stockmanagement.stockmanagement.stockbalance.entity.ProductStockBalance;
import com.stockmanagement.stockmanagement.stockbalance.repository.ProductStockBalanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductListingServiceImpl implements ProductListingService {

    private final ProductListingRepository listingRepository;
    private final ProductListingImageRepository imageRepository;
    private final ProductListingStockSourceRepository sourceRepository;
    private final ProductRepository productRepository;
    private final ProductStockBalanceRepository stockBalanceRepository;
    private final ProductListingMapper mapper;
    private final BusinessCodeService businessCodeService;

    @Override
    public ProductListingResponse create(ProductListingCreateRequest request) {

        Product product = productRepository
                .findByProductCodeAndDeletedFalse(request.getProductCode().trim())
                .orElseThrow(() -> notFound("Product not found"));

        validateActiveProduct(product);
        validateSlug(request.getSlug(), null);
        validatePrice(request.getSellingPrice(), request.getOnlineStockLimit());

        ProductListing listing = new ProductListing();
        listing.setListingCode(
                businessCodeService.generateCode(CodeType.PRODUCT_LISTING)
        );
        listing.setProduct(product);
        listing.setListingType(
                request.getListingType() == null
                        ? ListingType.ONLINE_STORE
                        : request.getListingType()
        );
        listing.setTitle(request.getTitle().trim());
        listing.setSlug(request.getSlug().trim());
        listing.setDescription(normalizeDescription(request.getDescription()));
        listing.setSellingPrice(request.getSellingPrice());
        listing.setOnlineStockLimit(request.getOnlineStockLimit());
        listing.setListingStatus(ListingStatus.DRAFT);

        if (request.getStockBalanceIds() != null) {
            replaceStockSources(listing, request.getStockBalanceIds());
        }

        return toResponse(listingRepository.save(listing));
    }

    @Override
    public ProductListingResponse update(
            String listingCode,
            ProductListingUpdateRequest request) {

        ProductListing listing = getListingOrThrow(listingCode);

        if (listing.getListingStatus() == ListingStatus.ARCHIVED) {
            throw badRequest("Archived listing cannot be updated");
        }

        validateSlug(request.getSlug(), listing.getId());
        validatePrice(request.getSellingPrice(), request.getOnlineStockLimit());

        listing.setTitle(request.getTitle().trim());
        listing.setSlug(request.getSlug().trim());
        listing.setDescription(normalizeDescription(request.getDescription()));
        listing.setSellingPrice(request.getSellingPrice());
        listing.setOnlineStockLimit(request.getOnlineStockLimit());

        if (request.getStockBalanceIds() != null) {
            replaceStockSources(listing, request.getStockBalanceIds());
        }

        /*
         * If a published listing's sellable configuration changes,
         * require republishing after validation.
         */
        if (listing.getListingStatus() == ListingStatus.PUBLISHED) {
            listing.setListingStatus(ListingStatus.DRAFT);
        }

        return toResponse(listingRepository.save(listing));
    }

    @Override
    public ProductListingResponse publish(String listingCode) {

        ProductListing listing = getListingOrThrow(listingCode);

        if (listing.getListingStatus() == ListingStatus.ARCHIVED) {
            throw badRequest("Archived listing cannot be published");
        }

        validateActiveProduct(listing.getProduct());
        validatePrice(listing.getSellingPrice(), listing.getOnlineStockLimit());

        List<ProductListingStockSource> sources =
                sourceRepository.findAllByListingIdAndEnabledTrueAndDeletedFalse(
                        listing.getId()
                );

        if (sources.isEmpty()) {
            throw badRequest(
                    "Add at least one stock source before publishing"
            );
        }

        BigDecimal availableQuantity = BigDecimal.ZERO;

        for (ProductListingStockSource source : sources) {
            ProductStockBalance balance = source.getStockBalance();

            if (balance == null || balance.isDeleted()) {
                throw badRequest("A selected stock balance is unavailable");
            }

            validateBalanceForProduct(balance, listing.getProduct());

            if (balance.getQuantity() != null
                    && balance.getQuantity().compareTo(BigDecimal.ZERO) > 0) {
                availableQuantity = availableQuantity.add(balance.getQuantity());
            }
        }

        if (listing.getOnlineStockLimit() != null) {
            availableQuantity = availableQuantity.min(
                    listing.getOnlineStockLimit()
            );
        }

        if (availableQuantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw badRequest("No positive stock is available for this listing");
        }

        listing.setListingStatus(ListingStatus.PUBLISHED);

        return toResponse(listingRepository.save(listing));
    }

    @Override
    public ProductListingResponse unpublish(String listingCode) {

        ProductListing listing = getListingOrThrow(listingCode);

        if (listing.getListingStatus() == ListingStatus.ARCHIVED) {
            throw badRequest("Archived listing cannot be unpublished");
        }

        listing.setListingStatus(ListingStatus.UNPUBLISHED);

        return toResponse(listingRepository.save(listing));
    }

    @Override
    @Transactional(readOnly = true)
    public ProductListingResponse getAdminByCode(String listingCode) {
        return toResponse(getListingOrThrow(listingCode));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductListingResponse> getAllAdmin(Pageable pageable) {

        validatePageable(pageable);

        return listingRepository.findAllByDeletedFalse(pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductListingCardResponse> getPublished(
            String keyword,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable) {

        validatePageable(pageable);
        validatePriceFilters(minPrice, maxPrice);

        String normalizedKeyword =
                keyword == null || keyword.isBlank() ? null : keyword.trim();

        return listingRepository.searchPublished(
                        ListingStatus.PUBLISHED,
                        ListingType.ONLINE_STORE,
                        normalizedKeyword,
                        minPrice,
                        maxPrice,
                        pageable
                )
                .map(this::toCardResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductListingResponse getPublishedByCode(String listingCode) {

        ProductListing listing = listingRepository.findPublishedDetail(
                        listingCode,
                        ListingStatus.PUBLISHED,
                        ListingType.ONLINE_STORE
                )
                .orElseThrow(() -> notFound("Published online listing not found"));

        validateActiveProduct(listing.getProduct());

        return toResponse(listing);
    }

    private ProductListing getListingOrThrow(String listingCode) {
        return listingRepository.findByListingCodeAndDeletedFalse(listingCode)
                .orElseThrow(() -> notFound("Product listing not found"));
    }

    private void validateActiveProduct(Product product) {
        if (product == null || product.isDeleted()
                || product.getStatus() != Status.ACTIVE) {
            throw badRequest("Only active, non-deleted products can be listed");
        }
    }

    private void validateSlug(String slug, Long currentId) {

        String normalizedSlug = slug.trim();

        boolean exists = currentId == null
                ? listingRepository.existsBySlugAndDeletedFalse(normalizedSlug)
                : listingRepository.existsBySlugAndIdNotAndDeletedFalse(
                normalizedSlug, currentId
        );

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Slug is already in use"
            );
        }
    }

    private void validatePrice(
            BigDecimal sellingPrice,
            BigDecimal onlineStockLimit) {

        if (sellingPrice == null
                || sellingPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw badRequest("Selling price must be greater than zero");
        }

        if (onlineStockLimit != null
                && onlineStockLimit.compareTo(BigDecimal.ZERO) <= 0) {
            throw badRequest("Online stock limit must be greater than zero");
        }
    }

    private void validatePriceFilters(
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        if (minPrice != null && minPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw badRequest("Minimum price cannot be negative");
        }

        if (maxPrice != null && maxPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw badRequest("Maximum price cannot be negative");
        }

        if (minPrice != null && maxPrice != null
                && minPrice.compareTo(maxPrice) > 0) {
            throw badRequest("Minimum price cannot exceed maximum price");
        }
    }

    private void validatePageable(Pageable pageable) {

        Set<String> allowedSorts = Set.of(
                "createdAt", "updatedAt", "title", "sellingPrice", "listingCode"
        );

        for (Sort.Order order : pageable.getSort()) {
            if (!allowedSorts.contains(order.getProperty())) {
                throw badRequest(
                        "Unsupported sort property: " + order.getProperty()
                );
            }
        }

        if (pageable.getPageSize() > 100) {
            throw badRequest("Page size cannot exceed 100");
        }
    }

    private void validateBalanceForProduct(
            ProductStockBalance balance,
            Product product) {

        if (balance.getProductKareegar() == null
                || balance.getProductKareegar().isDeleted()
                || balance.getProductKareegar().getProduct() == null
                || !Objects.equals(
                balance.getProductKareegar().getProduct().getId(),
                product.getId()
        )) {
            throw badRequest(
                    "Selected stock balance does not belong to this product"
            );
        }

        if (balance.getDesignKareegar() == null
                || balance.getDesignKareegar().isDeleted()) {
            throw badRequest(
                    "Selected stock balance has no valid design mapping"
            );
        }
    }

    private void replaceStockSources(
            ProductListing listing,
            List<Long> stockBalanceIds) {

        List<Long> ids = stockBalanceIds.stream().distinct().toList();

        if (ids.size() != stockBalanceIds.size()) {
            throw badRequest("Duplicate stock balance IDs are not allowed");
        }

        if (ids.isEmpty()) {
            listing.clearStockSources();
            return;
        }

        List<ProductStockBalance> balances =
                stockBalanceRepository.findAllById(ids);

        if (balances.size() != ids.size()) {
            throw notFound("One or more stock balances were not found");
        }

        Map<Long, ProductStockBalance> byId = balances.stream()
                .collect(Collectors.toMap(
                        ProductStockBalance::getId,
                        Function.identity()
                ));

        List<ProductStockBalance> ordered = ids.stream()
                .map(byId::get)
                .toList();

        ProductStockBalance first = ordered.get(0);

        for (ProductStockBalance balance : ordered) {

            if (balance.isDeleted()) {
                throw badRequest("Deleted stock balances cannot be selected");
            }

            validateBalanceForProduct(balance, listing.getProduct());

            if (!Objects.equals(
                    first.getDesignKareegar().getId(),
                    balance.getDesignKareegar().getId())
                    || first.getUnit() != balance.getUnit()) {
                throw badRequest(
                        "Stock sources must have the same design mapping and unit"
                );
            }
        }

        listing.clearStockSources();

        for (ProductStockBalance balance : ordered) {
            ProductListingStockSource source =
                    new ProductListingStockSource();

            source.setStockBalance(balance);
            source.setEnabled(true);
            listing.addStockSource(source);
        }
    }

    private ProductListingResponse toResponse(ProductListing listing) {

        ProductListingResponse response = mapper.toResponse(listing);

        response.setImages(
                imageRepository
                        .findAllByListingIdAndDeletedFalseOrderByDisplayOrderAsc(
                                listing.getId()
                        )
                        .stream()
                        .map(mapper::toImageResponse)
                        .toList()
        );

        return response;
    }

    private ProductListingCardResponse toCardResponse(
            ProductListing listing) {

        ProductListingCardResponse response = mapper.toCardResponse(listing);

        List<ProductListingImage> images =
                imageRepository
                        .findAllByListingIdAndDeletedFalseOrderByDisplayOrderAsc(
                                listing.getId()
                        );

        images.stream()
                .filter(ProductListingImage::isPrimaryImage)
                .findFirst()
                .or(() -> images.stream().findFirst())
                .ifPresent(image ->
                        response.setPrimaryImageUrl(image.getImageUrl())
                );

        return response;
    }

    private String normalizeDescription(String description) {
        return description == null || description.isBlank()
                ? null
                : description.trim();
    }

    private ResponseStatusException notFound(String message) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, message);
    }

    private ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}