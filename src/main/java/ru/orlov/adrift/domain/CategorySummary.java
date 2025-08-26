package ru.orlov.adrift.domain;

import java.io.Serializable;

public interface CategorySummary extends Serializable {

    Long getId();

    String getName();

    String getSlug();

    ParentSummary getParent();

    interface ParentSummary extends Serializable {
        Long getId();
    }
}
