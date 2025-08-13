package ru.orlov.adrift.controller.dto;

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

    @NotBlank
    private String title;

    private String description;

    @Min(0)
    private BigDecimal price;

    @NotNull
    @Min(1)
    private Long category;

    private List<AdFieldDto> fields = new ArrayList<>();

    @Data
    @AllArgsConstructor
    public static class AdFieldDto {

        @NotNull
        private Long qid;

        private String value;
    }

}
