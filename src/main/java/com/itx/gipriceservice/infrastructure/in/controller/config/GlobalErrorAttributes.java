package com.itx.gipriceservice.infrastructure.in.controller.config;

import com.itx.gipriceservice.domain.exception.PriceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webflux.error.DefaultErrorAttributes;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

record ExceptionRule(Class<?> exceptionClass, HttpStatus status) {
}

@Slf4j
@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private final List<ExceptionRule> exceptionsRules = List.of(
            new ExceptionRule(PriceNotFoundException.class, HttpStatus.NOT_FOUND)
    );

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Throwable error = getError(request);

        final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        Optional<ExceptionRule> exceptionRuleOptional = exceptionsRules.stream()
                .filter(rule -> rule.exceptionClass().isInstance(error))
                .findFirst();

        return exceptionRuleOptional.<Map<String, Object>>map(exceptionRule -> {
            log.warn("Business exception handled: [HTTP {}] {}", exceptionRule.status().value(), error.getMessage());
            return Map.of(ErrorAttributesKey.CODE.getKey(), exceptionRule.status().value(),
                    ErrorAttributesKey.MESSAGE.getKey(), error.getMessage(),
                    ErrorAttributesKey.TIME.getKey(), timestamp);
        }).orElseGet(() -> {
            final HttpStatus status = determineHttpStatus(error);
            if (status.is5xxServerError()) {
                log.error("Unhandled server error: [HTTP {}] {}", status.value(), error.getMessage(), error);
            } else {
                log.warn("Unhandled client error: [HTTP {}] {}", status.value(), error.getMessage());
            }
            return Map.of(ErrorAttributesKey.CODE.getKey(), status.value(),
                    ErrorAttributesKey.MESSAGE.getKey(), error.getMessage(),
                    ErrorAttributesKey.TIME.getKey(), timestamp);
        });
    }

    private HttpStatus determineHttpStatus(Throwable error) {
        if (error instanceof ResponseStatusException err) {
            return HttpStatus.valueOf(err.getStatusCode().value());
        }
        var annotation = MergedAnnotations.from(error.getClass(), MergedAnnotations.SearchStrategy.TYPE_HIERARCHY)
                .get(ResponseStatus.class);
        return annotation.getValue(ErrorAttributesKey.CODE.getKey(), HttpStatus.class)
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
