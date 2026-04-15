package com.itx.gipriceservice.domain.service;

import com.itx.gipriceservice.domain.exception.PriceNotFoundException;
import com.itx.gipriceservice.domain.model.Price;
import com.itx.gipriceservice.domain.model.PriceFilter;

import java.util.List;

/**
 * Default pricing rule: selects the candidate with the highest PRIORITY value.
 * When two or more price entries overlap in date range, the one with the
 * greatest numeric priority wins (per the domain specification).
 *
 * This rule always declares itself applicable, acting as the catch-all
 * fallback at the end of the rules chain.
 */
public class HighestPriorityPricingRule implements PricingRule {

    @Override
    public boolean appliesTo(PriceFilter filter) {
        return true;
    }

    @Override
    public Price apply(List<Price> candidates, PriceFilter filter) {
        return candidates.stream()
                .reduce((first, second) -> first.hasHigherPriorityThan(second) ? first : second)
                .orElseThrow(() -> new PriceNotFoundException(filter.getBrandId(), filter.getProductId()));
    }

}
