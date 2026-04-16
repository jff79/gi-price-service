package com.itx.gipriceservice.infrastructure.out.persistence.repository;

import com.itx.gipriceservice.infrastructure.out.persistence.entity.PriceEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.OffsetDateTime;

@Repository
public interface PriceDataRepository extends ReactiveSortingRepository<PriceEntity, Long> {

    Flux<PriceEntity> findByStartDateLessThanEqualAndEndDateGreaterThanAndProductIdAndBrandId(
            OffsetDateTime startDate, OffsetDateTime endDate, Long productId, Integer brandId);

}
