package com.itx.gipriceservice.domain.service;

import com.itx.gipriceservice.domain.model.Price;
import com.itx.gipriceservice.domain.model.PriceFilter;

import java.util.List;

/**
 * Strategy interface for pricing rules.
 * Each implementation encapsulates a single, independently testable rule
 * for selecting the applicable price from a list of candidates.
 *
 * New pricing strategies can be added by implementing this interface
 * and registering the bean in the application configuration —
 * no existing code needs to change (Open/Closed Principle).
 */
public interface PricingRule {

    /**
     * Returns true if this rule can handle the given filter context.
     * Rules are evaluated in order; the first applicable rule wins.
     */
    boolean appliesTo(PriceFilter filter);

    /**
     * Selects and returns the winning price from the list of candidates.
     * Throws {@link com.itx.gipriceservice.domain.exception.PriceNotFoundException}
     * if no suitable price can be determined.
     */
    Price apply(List<Price> candidates, PriceFilter filter);

}
