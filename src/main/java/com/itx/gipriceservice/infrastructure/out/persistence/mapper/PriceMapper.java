package com.itx.gipriceservice.infrastructure.out.persistence.mapper;

import com.itx.gipriceservice.domain.model.Price;
import com.itx.gipriceservice.infrastructure.out.persistence.entity.PriceEntity;
import org.mapstruct.Mapper;

@Mapper
public interface PriceMapper {

    Price from(PriceEntity priceEntity);

}
