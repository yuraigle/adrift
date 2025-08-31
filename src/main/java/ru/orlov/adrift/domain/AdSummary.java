package ru.orlov.adrift.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AdSummary extends Serializable {

    Long getId();

    String getTitle();

    String getDescription();

    BigDecimal getPrice();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime getCreated();

    UserSummary getUser();

    CategorySummary getCategory();

    List<AdImageSummary> getImages();

    interface UserSummary extends Serializable {
        Long getId();

        String getUsername();
    }

    interface AdImageSummary extends Serializable {
        Long getId();
        String getFilename();
        String getAlt();
    }

}
