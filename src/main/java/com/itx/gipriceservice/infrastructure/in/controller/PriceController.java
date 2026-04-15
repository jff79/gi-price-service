package com.itx.gipriceservice.infrastructure.in.controller;

import com.itx.gipriceservice.application.usecase.GetPriceUseCase;
import com.itx.gipriceservice.domain.model.PriceFilter;
import com.itx.gipriceservice.infrastructure.in.api.model.PriceResponse;
import com.itx.gipriceservice.infrastructure.in.api.rest.PriceControllerApi;
import com.itx.gipriceservice.infrastructure.in.controller.mapper.PriceResponseMapper;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class PriceController implements PriceControllerApi {

    private final GetPriceUseCase getPriceUseCase;
    private final PriceResponseMapper priceResponseMapper;

    @Override
    public Mono<ResponseEntity<PriceResponse>> getPrice(
            OffsetDateTime at, Long productId, Integer brandId, ServerWebExchange exchange) {
        log.info("GET /price - at={}, productId={}, brandId={}", at, productId, brandId);
        return getPriceUseCase.execute(new PriceFilter(at, productId, brandId))
                .map(price -> ResponseEntity.ok(priceResponseMapper.from(price)))
                .doOnSuccess(response -> log.debug("Response: HTTP {} - price={}",
                        response.getStatusCode(),
                        response.getBody() != null ? response.getBody().getCost() : null))
                .doOnError(e -> log.debug("Request ended with error: {}", e.getMessage()));
    }

}
