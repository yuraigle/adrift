package ru.orlov.adrift.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.orlov.adrift.domain.AdSummary;
import ru.orlov.adrift.domain.CategoryRepository;
import ru.orlov.adrift.domain.CategorySummary;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.service.AdSearchService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final AdSearchService adSearchService;

    @GetMapping(value = "/api/categories", produces = "application/json")
    public List<CategorySummary> list() throws AppException {
        return categoryRepository.findAllSummary();
    }

    @GetMapping(value = "/api/categories/{cid}/a", produces = "application/json")
    public Page<AdSummary> listAds(
            @PathVariable Long cid,
            @PageableDefault(size = 5) Pageable pageable
    ) throws AppException {
        return adSearchService.listByCategory(cid, pageable);
    }

}
