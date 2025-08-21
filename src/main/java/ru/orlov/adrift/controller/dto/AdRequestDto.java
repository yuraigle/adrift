package ru.orlov.adrift.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class AdRequestDto {

    @NotBlank(message = "Title must be set")
    private String title;

    private String description;

    @NotNull(message = "Price must be set")
    @Min(value = 0, message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Category must be set")
    private Long category;

    private List<@Valid AdFieldDto> fields = new ArrayList<>();

    @Data
    @AllArgsConstructor
    public static class AdFieldDto {

        @NotNull
        private Long qid;

        private String value;
    }

}
