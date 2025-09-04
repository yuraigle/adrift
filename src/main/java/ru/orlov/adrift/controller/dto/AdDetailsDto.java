package ru.orlov.adrift.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class AdDetailsDto {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated;

    private List<AdFieldDto> fields = new ArrayList<>();

    @Data
    @AllArgsConstructor
    public static class AdFieldDto {
        private Long qid;
        private String value;
    }

}
