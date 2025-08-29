package ru.orlov.adrift.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.orlov.adrift.domain.AdSummary;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.service.AdSearchService;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final AdSearchService adSearchService;

    @GetMapping(value = "/api/recommended", produces = "application/json")
    public Page<AdSummary> recommended(
            @PageableDefault(size = 24) Pageable pageable
    ) throws AppException {
        return adSearchService.listRecommended(pageable);
    }
}
