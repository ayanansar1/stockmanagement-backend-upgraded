package com.stockmanagement.stockmanagement.buyer.mapper;

import com.stockmanagement.stockmanagement.buyer.dto.BuyerResponse;
import com.stockmanagement.stockmanagement.buyer.entity.Buyer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BuyerMapper {

    BuyerResponse toResponse(Buyer buyer);
}