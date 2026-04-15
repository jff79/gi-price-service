package com.itx.gipriceservice.application.usecase;

import com.itx.gipriceservice.domain.model.Price;
import com.itx.gipriceservice.domain.model.PriceFilter;
import com.itx.gipriceservice.domain.port.PriceRepository;
import com.itx.gipriceservice.domain.service.PricingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
public class GetPriceUseCase {

    private final PriceRepository priceRepository;
    private final PricingService pricingService;

    public Mono<Price> execute(PriceFilter priceFilter) {
        log.debug("Executing GetPriceUseCase - brandId={}, productId={}, at={}",
                priceFilter.getBrandId(), priceFilter.getProductId(), priceFilter.getAt());
        return priceRepository.findApplicablePrices(priceFilter)
                .collectList()
                .doOnNext(prices -> log.debug("Applicable prices after date filter: {}", prices.size()))
                .map(prices -> pricingService.selectBestPrice(prices, priceFilter))
                .doOnSuccess(price -> log.info("Best price selected: feeId={}, price={}",
                        price.getFeeId(), price.getCost()));
    }

}
