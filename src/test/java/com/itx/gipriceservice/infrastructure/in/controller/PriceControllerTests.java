package com.itx.gipriceservice.infrastructure.in.controller;

import com.itx.gipriceservice.infrastructure.in.api.model.PriceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PriceControllerTests {

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @BeforeEach
    void setup() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void givenBrandIdProductIdAndDate20200614100000_whenGetPrice_thenStatus200() {
        executeGetPrice(
                "2020-06-14T10:00:00.000Z",
                1,
                OffsetDateTime.parse("2020-06-14T00:00:00Z"),
                OffsetDateTime.parse("2021-01-01T00:00:00Z"),
                35.5
        );
    }

    @Test
    void givenBrandIdProductIdAndDate20200614160000_whenGetPrice_thenStatus200() {
        executeGetPrice(
                "2020-06-14T16:00:00.000Z",
                2,
                OffsetDateTime.parse("2020-06-14T15:00:00Z"),
                OffsetDateTime.parse("2020-06-14T18:30:00Z"),
                25.45
        );
    }

    @Test
    void givenBrandIdProductIdAndDate20200614210000_whenGetPrice_thenStatus200() {
        executeGetPrice(
                "2020-06-14T21:00:00.000Z",
                1,
                OffsetDateTime.parse("2020-06-14T00:00:00Z"),
                OffsetDateTime.parse("2021-01-01T00:00:00Z"),
                35.5
        );
    }

    @Test
    void givenBrandIdProductIdAndDate20200615100000_whenGetPrice_thenStatus200() {
        executeGetPrice(
                "2020-06-15T10:00:00.000Z",
                3,
                OffsetDateTime.parse("2020-06-15T00:00:00Z"),
                OffsetDateTime.parse("2020-06-15T11:00:00Z"),
                30.5
        );
    }

    @Test
    void givenBrandIdProductIdAndDate20200616210000_whenGetPrice_thenStatus200() {
        executeGetPrice(
                "2020-06-16T21:00:00.000Z",
                4,
                OffsetDateTime.parse("2020-06-15T16:00:00Z"),
                OffsetDateTime.parse("2021-01-01T00:00:00Z"),
                38.95
        );
    }

    @Test
    void givenBrandIdProductId_whenGetPrice_thenStatus400() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1.0/price")
                        .queryParam("brandId", "1")
                        .queryParam("productId", "35455")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void givenNoMatchingPrice_whenGetPrice_thenStatus404() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1.0/price")
                        .queryParam("brandId", "1")
                        .queryParam("productId", "35455")
                        .queryParam("at", "2000-01-01T00:00:00.000Z")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    private void executeGetPrice(String at, Integer expectedFeeId,
                                 OffsetDateTime expectedStartDate, OffsetDateTime expectedEndDate,
                                 Double expectedCost) {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1.0/price")
                        .queryParam("brandId", "1")
                        .queryParam("productId", "35455")
                        .queryParam("at", at)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(PriceResponse.class)
                .value(response -> {
                    assertEquals(1, response.getBrandId());
                    assertEquals(35455L, response.getProductId());
                    assertEquals(expectedFeeId, response.getFeeId());
                    assertTrue(response.getStartDate().isEqual(expectedStartDate),
                            "startDate mismatch: expected " + expectedStartDate + " but was " + response.getStartDate());
                    assertTrue(response.getEndDate().isEqual(expectedEndDate),
                            "endDate mismatch: expected " + expectedEndDate + " but was " + response.getEndDate());
                    assertEquals(expectedCost, response.getCost());
                });
    }

}
