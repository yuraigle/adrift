package ru.orlov.adrift.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdRepository extends JpaRepository<Ad, Long> {

    Optional<AdSummary> findAdSummaryById(Long id);

}