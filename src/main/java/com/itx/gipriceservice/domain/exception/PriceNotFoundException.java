package com.itx.gipriceservice.domain.exception;

public class PriceNotFoundException extends RuntimeException {

    public PriceNotFoundException(Integer brandId, Long productId) {
        super(String.format("No applicable price found for brandId=%d and productId=%d", brandId, productId));
    }

}
