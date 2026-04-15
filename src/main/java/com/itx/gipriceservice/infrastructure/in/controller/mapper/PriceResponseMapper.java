package com.itx.gipriceservice.infrastructure.in.controller.mapper;

import com.itx.gipriceservice.domain.model.Price;
import com.itx.gipriceservice.infrastructure.in.api.model.PriceResponse;
import org.mapstruct.Mapper;

@Mapper
public interface PriceResponseMapper {

    PriceResponse from(Price price);

}
