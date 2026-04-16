package com.itx.gipriceservice.infrastructure.out.persistence.repository;

import com.itx.gipriceservice.domain.port.PriceRepository;
import com.itx.gipriceservice.domain.model.Price;
import com.itx.gipriceservice.domain.model.PriceFilter;
import com.itx.gipriceservice.infrastructure.out.persistence.mapper.PriceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PriceRepositoryImpl implements PriceRepository {

    private final PriceDataRepository priceDataRepository;
    private final PriceMapper priceMapper;

    @Override
    public Flux<Price> findApplicablePrices(PriceFilter priceFilter) {
        log.debug("Querying PRICES - brandId={}, productId={}, at={}",
                priceFilter.getBrandId(), priceFilter.getProductId(), priceFilter.getAt());
        return priceDataRepository
                .findByStartDateLessThanEqualAndEndDateGreaterThanAndProductIdAndBrandId(
                        priceFilter.getAt(), priceFilter.getAt(),
                        priceFilter.getProductId(), priceFilter.getBrandId())
                .doOnNext(entity -> log.debug("Row fetched: id={}, feeId={}, priority={}, price={}",
                        entity.getId(), entity.getFeeId(), entity.getPriority(), entity.getCost()))
                .map(priceMapper::from);
    }

}
