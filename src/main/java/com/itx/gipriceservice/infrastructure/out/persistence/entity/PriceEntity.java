package com.itx.gipriceservice.infrastructure.out.persistence.entity;

import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Table(name = "PRICES")
@Value
public class PriceEntity {

    @Id
    @Column("ID")
    Long id;

    @Column("BRAND_ID")
    Integer brandId;

    @Column("START_DATE")
    OffsetDateTime startDate;

    @Column("END_DATE")
    OffsetDateTime endDate;

    @Column("FEE_ID")
    Integer feeId;

    @Column("PRODUCT_ID")
    Long productId;

    @Column("PRIORITY")
    Integer priority;

    @Column("COST")
    BigDecimal cost;

    @Column("CURR")
    String curr;

}
