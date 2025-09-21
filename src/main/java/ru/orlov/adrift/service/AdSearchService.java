package ru.orlov.adrift.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.orlov.adrift.controller.dto.SearchFilterDto;
import ru.orlov.adrift.domain.AdRepository;
import ru.orlov.adrift.domain.AdSummary;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdSearchService {

    private final AdRepository adRepository;

    public Page<AdSummary> listByCategory(Long categoryId, SearchFilterDto filter, Pageable pageable) {
        if (filter != null) {
            return adRepository.findIdsByCategoryFiltered(
                    categoryId,
                    filter.getPriceFrom() == null ? 0 : filter.getPriceFrom(),
                    filter.getPriceTo() == null ? Long.MAX_VALUE : filter.getPriceTo(),
                    filter.getKeywords() == null ? "" : filter.getKeywords(),
                    pageable
            );
        }

        return adRepository.findAllByCategoryIdOrderByCreatedDesc(categoryId, pageable);
    }

    public Page<AdSummary> listRecommended(Pageable pageable) {
        return adRepository.findAllOrderByCreatedDesc(pageable);
    }

}
