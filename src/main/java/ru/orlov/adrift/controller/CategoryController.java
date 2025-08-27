package ru.orlov.adrift.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.orlov.adrift.domain.AdSummary;
import ru.orlov.adrift.domain.CategoryRepository;
import ru.orlov.adrift.domain.CategorySummary;
import ru.orlov.adrift.domain.ex.AppAuthException;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.service.AdSearchService;

import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping(value = "/api/categories/crawl", produces = "application/json")
    public ResponseEntity<List<String>> crawlUrls(
            @RequestHeader("Authorization") String token
    ) throws AppException {
        if (token == null || !token.equals("CRAWLER")) {
            throw new AppAuthException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        List<String> urls = categoryRepository.findAll().stream()
                .map(c -> "/" + c.getSlug())
                .collect(Collectors.toList());

        return new ResponseEntity<>(urls, null, HttpStatus.OK);
    }

}
