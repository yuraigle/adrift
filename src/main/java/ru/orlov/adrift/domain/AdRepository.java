package ru.orlov.adrift.domain;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AdRepository extends JpaRepository<Ad, Long> {

    List<Ad> findByTitle(String title);

    List<Ad> findAllByTitleStartingWith(String title);

    @Query("SELECT a.id FROM Ad a")
    List<Long> getAllIds();

    @Query("SELECT a FROM Ad a LEFT JOIN FETCH a.images WHERE a.id = :id")
    Optional<AdSummary> findAdSummaryById(Long id);

    @Query("SELECT a FROM Ad a LEFT JOIN FETCH a.images WHERE a.category.id = :categoryId ORDER BY a.created DESC")
    Page<AdSummary> findAllByCategoryIdOrderByCreatedDesc(Long categoryId, Pageable pageable);

    @Query("""
            SELECT a FROM Ad a LEFT JOIN FETCH a.images
            WHERE a.category.id = :categoryId
                and a.price >= :minPrice and a.price <= :maxPrice
                and a.title like %:keywords%
            ORDER BY a.created DESC
            """)
    Page<AdSummary> findIdsByCategoryFiltered(
            Long categoryId,
            Long minPrice,
            Long maxPrice,
            String keywords,
            Pageable pageable
    );

    @Query("SELECT a FROM Ad a LEFT JOIN FETCH a.images ORDER BY a.created DESC")
    Page<AdSummary> findAllOrderByCreatedDesc(Pageable pageable);

    @Transactional
    @Query("""
            select a from Ad a
                left join fetch a.images
            where a.id = :id
            """)
    Optional<Ad> findAdWithFetchById(Long id);

}
