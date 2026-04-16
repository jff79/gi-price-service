package com.itx.gipriceservice.infrastructure.in.api.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PriceResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-04-16T02:02:34.909684900+02:00[Europe/Madrid]", comments = "Generator version: 7.21.0")
public class PriceResponse {

  private Integer brandId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime endDate;

  private Integer feeId;

  private Long productId;

  private Double cost;

  public PriceResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PriceResponse(Integer brandId, OffsetDateTime startDate, OffsetDateTime endDate, Integer feeId, Long productId, Double cost) {
    this.brandId = brandId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.feeId = feeId;
    this.productId = productId;
    this.cost = cost;
  }

  public PriceResponse brandId(Integer brandId) {
    this.brandId = brandId;
    return this;
  }

  /**
   * Get brandId
   * minimum: 1
   * @return brandId
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "brandId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("brandId")
  public Integer getBrandId() {
    return brandId;
  }

  @JsonProperty("brandId")
  public void setBrandId(Integer brandId) {
    this.brandId = brandId;
  }

  public PriceResponse startDate(OffsetDateTime startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Get startDate
   * @return startDate
   */
  @NotNull @Valid 
  @Schema(name = "startDate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startDate")
  public OffsetDateTime getStartDate() {
    return startDate;
  }

  @JsonProperty("startDate")
  public void setStartDate(OffsetDateTime startDate) {
    this.startDate = startDate;
  }

  public PriceResponse endDate(OffsetDateTime endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * Get endDate
   * @return endDate
   */
  @NotNull @Valid 
  @Schema(name = "endDate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("endDate")
  public OffsetDateTime getEndDate() {
    return endDate;
  }

  @JsonProperty("endDate")
  public void setEndDate(OffsetDateTime endDate) {
    this.endDate = endDate;
  }

  public PriceResponse feeId(Integer feeId) {
    this.feeId = feeId;
    return this;
  }

  /**
   * Get feeId
   * minimum: 1
   * @return feeId
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "feeId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("feeId")
  public Integer getFeeId() {
    return feeId;
  }

  @JsonProperty("feeId")
  public void setFeeId(Integer feeId) {
    this.feeId = feeId;
  }

  public PriceResponse productId(Long productId) {
    this.productId = productId;
    return this;
  }

  /**
   * Get productId
   * minimum: 1
   * @return productId
   */
  @NotNull @Min(value = 1L) 
  @Schema(name = "productId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("productId")
  public Long getProductId() {
    return productId;
  }

  @JsonProperty("productId")
  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public PriceResponse cost(Double cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Get cost
   * minimum: 0
   * @return cost
   */
  @NotNull @DecimalMin(value = "0") 
  @Schema(name = "cost", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cost")
  public Double getCost() {
    return cost;
  }

  @JsonProperty("cost")
  public void setCost(Double cost) {
    this.cost = cost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PriceResponse priceResponse = (PriceResponse) o;
    return Objects.equals(this.brandId, priceResponse.brandId) &&
        Objects.equals(this.startDate, priceResponse.startDate) &&
        Objects.equals(this.endDate, priceResponse.endDate) &&
        Objects.equals(this.feeId, priceResponse.feeId) &&
        Objects.equals(this.productId, priceResponse.productId) &&
        Objects.equals(this.cost, priceResponse.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(brandId, startDate, endDate, feeId, productId, cost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PriceResponse {\n");
    sb.append("    brandId: ").append(toIndentedString(brandId)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    feeId: ").append(toIndentedString(feeId)).append("\n");
    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

