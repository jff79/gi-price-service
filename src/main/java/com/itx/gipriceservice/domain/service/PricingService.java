package com.itx.gipriceservice.domain.service;

import com.itx.gipriceservice.domain.exception.PriceNotFoundException;
import com.itx.gipriceservice.domain.model.Price;
import com.itx.gipriceservice.domain.model.PriceFilter;

import java.util.List;

/**
 * Pricing rules engine.
 * Evaluates an ordered list of {@link PricingRule} strategies and delegates
 * selection to the first rule that declares itself applicable to the given
 * filter context.
 *
 * Adding a new pricing strategy requires only:
 *   1. Implementing {@link PricingRule}.
 *   2. Registering it as a bean (with the desired order) in the
 *      application configuration.
 * No changes to this class or any other existing class are needed.
 */
public class PricingService {

    private final List<PricingRule> rules;

    public PricingService(List<PricingRule> rules) {
        this.rules = rules;
    }

    public Price selectBestPrice(List<Price> prices, PriceFilter filter) {
        return rules.stream()
                .filter(rule -> rule.appliesTo(filter))
                .findFirst()
                .map(rule -> rule.apply(prices, filter))
                .orElseThrow(() -> new PriceNotFoundException(filter.getBrandId(), filter.getProductId()));
    }

}
