package ru.orlov.adrift.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SearchFilterDto {

    @Min(0)
    @JsonProperty(value = "price_from")
    private Long priceFrom;

    @Min(0)
    @JsonProperty(value = "price_to")
    private Long priceTo;

    @Size(max = 100)
    private String keywords;

}
