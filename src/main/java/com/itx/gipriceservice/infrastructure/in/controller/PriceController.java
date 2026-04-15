package com.itx.gipriceservice.infrastructure.in.controller;

import com.itx.gipriceservice.infrastructure.in.api.model.PriceResponse;
import com.itx.gipriceservice.infrastructure.in.api.rest.PriceControllerApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Slf4j
@Validated
@RestController
public class PriceController implements PriceControllerApi {

    // TODO: inject GetPriceUseCase (application layer — pending implementation)

    @Override
    public Mono<ResponseEntity<PriceResponse>> getPrice(
            OffsetDateTime at, Long productId, Integer brandId, ServerWebExchange exchange) {
        // TODO: delegate to GetPriceUseCase once domain and application layers are in place
        return Mono.error(new UnsupportedOperationException("Not yet implemented"));
    }

}