package ru.orlov.adrift.controller.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AdRequestDto {

    @NotBlank
    private String title;

    private String description;
    private BigDecimal price;

    @NotNull
    @Min(1)
    private Long category;

}
