package ru.orlov.adrift.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AdSummary {

    Long getId();
    String getTitle();
    String getDescription();
    BigDecimal getPrice();
    String getWww();
    String getContact();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime getCreated();

    UserSummary getUser();
    CategorySummary getCategory();

    interface UserSummary {
        Long getId();
        String getUsername();
    }

    interface CategorySummary {
        Long getId();
        String getName();
    }

}
