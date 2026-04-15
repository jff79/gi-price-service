package com.itx.gipriceservice.domain.port;

import com.itx.gipriceservice.domain.model.Price;
import com.itx.gipriceservice.domain.model.PriceFilter;
import reactor.core.publisher.Flux;

public interface PriceRepository {

    Flux<Price> findApplicablePrices(PriceFilter priceFilter);

}
