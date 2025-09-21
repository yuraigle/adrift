package ru.orlov.adrift.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.orlov.adrift.controller.dto.SearchFilterDto;
import ru.orlov.adrift.domain.AdRepository;
import ru.orlov.adrift.domain.AdSummary;

@Service
@RequiredArgsConstructor
public class AdSearchService {

    private final AdRepository adRepository;

    public Page<AdSummary> listByCategory(Long categoryId, SearchFilterDto filter, Pageable pageable) {
        if (filter != null) {
            // todo: apply filter
        }

        return adRepository.findAllByCategoryIdOrderByCreatedDesc(categoryId, pageable);
    }

    public Page<AdSummary> listRecommended(Pageable pageable) {
        return adRepository.findAllOrderByCreatedDesc(pageable);
    }

}
