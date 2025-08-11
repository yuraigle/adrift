package ru.orlov.adrift.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdRepository extends JpaRepository<Ad, Long> {

    Optional<AdSummary> findAdSummaryById(Long id);

    @Query("SELECT a FROM Ad a WHERE a.title like 'Test AD%'")
    List<Ad> findAllTestAds();

}