package com.itx.gipriceservice.domain.model;

import lombok.Value;

import java.time.OffsetDateTime;

@Value
public class PriceFilter {

    private OffsetDateTime at;

    private Long productId;

    private Integer brandId;

}
