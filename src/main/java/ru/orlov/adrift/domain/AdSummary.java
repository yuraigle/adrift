package ru.orlov.adrift.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AdSummary extends Serializable {

    Long getId();

    String getTitle();

    String getDescription();

    BigDecimal getPrice();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime getCreated();

    UserSummary getUser();

    CategorySummary getCategory();

    interface UserSummary extends Serializable {
        Long getId();

        String getUsername();
    }

    interface CategorySummary extends Serializable {
        Long getId();

        String getName();
    }

}
