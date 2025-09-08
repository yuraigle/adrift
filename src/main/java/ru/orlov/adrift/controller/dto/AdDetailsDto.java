package ru.orlov.adrift.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.orlov.adrift.domain.AdImage;
import ru.orlov.adrift.domain.Category;
import ru.orlov.adrift.domain.User;

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
    private String phone; // todo: hide it
    private String city;
    private String zip;
    private String address;
    private BigDecimal lat;
    private BigDecimal lon;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated;

    private UserDto user;
    private CategoryDto category;
    private List<AdFieldDto> fields = new ArrayList<>();
    private List<AdImageDto> images = new ArrayList<>();

    @Data
    public static class UserDto {
        private Long id;
        private String username;

        public UserDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
        }
    }

    @Data
    public static class CategoryDto {
        private Long id;
        private String name;
        private String slug;

        public CategoryDto(Category category) {
            this.id = category.getId();
            this.name = category.getName();
            this.slug = category.getSlug();
        }
    }

    @Data
    @AllArgsConstructor
    public static class AdFieldDto {
        private Long qid;
        private String value;
    }

    @Data
    public static class AdImageDto {
        private String filename;
        private String alt;

        public AdImageDto(AdImage image) {
            this.filename = image.getFilename();
            this.alt = image.getAlt();
        }
    }
}
