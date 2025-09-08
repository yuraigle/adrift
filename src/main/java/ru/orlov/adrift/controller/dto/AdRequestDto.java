package ru.orlov.adrift.controller.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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

    @Length(max = 20)
    private String phone;

    @NotNull(message = "Category must be set")
    private Long category;

    @Valid
    private AddressDto address;

    private List<@Valid AdFieldDto> fields = new ArrayList<>();

    @Data
    @AllArgsConstructor
    public static class AdFieldDto {

        @NotNull
        private Long qid;

        private String value;
    }

    @Data
    public static class AddressDto {

        @Length(max = 50)
        private String city;

        @Length(max = 20)
        private String zip;

        @Length(max = 255)
        private String address;

        @Min(-90)
        @Max(90)
        @Digits(integer = 2, fraction = 8)
        private BigDecimal lat;

        @Min(-180)
        @Max(180)
        @Digits(integer = 3, fraction = 8)
        private BigDecimal lon;
    }
}
