package com.itx.gipriceservice.domain.model;

import lombok.Value;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Value
public class Price {

    Integer brandId;
    OffsetDateTime startDate;
    OffsetDateTime endDate;
    Integer feeId;
    Long productId;
    BigDecimal cost;
    Integer priority;

    public boolean hasHigherPriorityThan(Price other) {
        return this.priority > other.priority;
    }

}
