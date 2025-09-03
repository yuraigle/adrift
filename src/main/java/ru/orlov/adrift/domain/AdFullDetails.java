package ru.orlov.adrift.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AdFullDetails extends Serializable {

    Long getId();

    String getTitle();

    String getDescription();

    BigDecimal getPrice();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime getCreated();

    UserSummary getUser();

    CategorySummary getCategory();

    List<AdFieldSummary> getFields();

    List<AdOptionSummary> getOptions();

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

    interface AdFieldSummary extends Serializable {
        QuestionSummary getQuestion();
        Integer getValNumber();
        BigDecimal getValDecimal();
        String getValText();
    }

    interface AdOptionSummary extends Serializable {
        QuestionSummary getQuestion();
        OptionSummary getOption();
    }

    interface OptionSummary extends Serializable {
        Long getId();
        String getName();
    }

    interface QuestionSummary extends Serializable {
        Long getId();
        String getName();
    }
}
