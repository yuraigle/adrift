package ru.orlov.adrift.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("select c from Category c")
    List<CategorySummary> findAllSummary();

    @Query("select c from Category c where c.id = :id")
    Optional<CategorySummary> findSummaryById(Long id);

    @Query("select c from Category c where c.slug = :slug")
    Optional<CategorySummary> findSummaryBySlug(String slug);

}
