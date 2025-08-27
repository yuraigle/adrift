package ru.orlov.adrift.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdRepository extends JpaRepository<Ad, Long> {

    Optional<AdSummary> findAdSummaryById(Long id);

    List<Ad> findByTitle(String title);

    List<Ad> findAllByTitleStartingWith(String title);

    Page<AdSummary> findAllByCategoryIdOrderByCreatedDesc(Long categoryId, Pageable pageable);

    @Query("SELECT a.id FROM Ad a")
    List<Long> getAllIds();

}
