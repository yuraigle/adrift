package ru.orlov.adrift.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.orlov.adrift.domain.AdRepository;
import ru.orlov.adrift.domain.CategoryRepository;
import ru.orlov.adrift.domain.ex.AppAuthException;
import ru.orlov.adrift.domain.ex.AppException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper routes to provide URLs list for SSR
 */

@RestController
@RequiredArgsConstructor
public class CrawlerController {

    private final AdRepository adRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping(value = "/api/ads/crawl", produces = "application/json")
    public ResponseEntity<List<String>> crawlAdUrls(
            @RequestHeader("Authorization") String token
    ) throws AppException {
        checkCrawlerToken(token);

        List<String> urls = adRepository.getAllIds().stream()
                .map(id -> "/" + id)
                .collect(Collectors.toList());

        return new ResponseEntity<>(urls, null, HttpStatus.OK);
    }

    @GetMapping(value = "/api/categories/crawl", produces = "application/json")
    public ResponseEntity<List<String>> crawlCategoryUrls(
            @RequestHeader("Authorization") String token
    ) throws AppException {
        checkCrawlerToken(token);

        List<String> urls = categoryRepository.findAll().stream()
                .map(c -> "/" + c.getSlug())
                .collect(Collectors.toList());

        return new ResponseEntity<>(urls, null, HttpStatus.OK);
    }

    private void checkCrawlerToken(String token) throws AppAuthException {
        if (token == null || !token.equals("CRAWLER")) {
            throw new AppAuthException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

}
