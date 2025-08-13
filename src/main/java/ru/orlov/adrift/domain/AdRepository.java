package ru.orlov.adrift.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdRepository extends JpaRepository<Ad, Long> {

    Optional<AdSummary> findAdSummaryById(Long id);

    List<Ad> findByTitle(String title);

    List<Ad> findAllByTitleLike(String title);

}
