package ru.orlov.adrift.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.orlov.adrift.controller.dto.AdRequestDto;
import ru.orlov.adrift.domain.*;
import ru.orlov.adrift.domain.ex.AppAuthException;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.service.AdService;
import ru.orlov.adrift.service.AuthService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;
    private final AuthService authService;
    private final AdRepository adRepository;
    private final UserRepository userRepository;

    @GetMapping(value = "/api/ads/{id}", produces = "application/json")
    public AdSummary show(@PathVariable Long id) throws AppException {
        return adRepository.findAdSummaryById(id)
                .orElseThrow(() -> new AppException("Ad not found", 404));
    }

    @PostMapping(value = "/api/ads", produces = "application/json")
    public ResponseEntity<AdSummary> create(
            @RequestHeader("Authorization") String token,
            @Valid @RequestBody AdRequestDto request
    ) throws AppException {
        Long userId = authService.parseToken(token).getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppAuthException("User not found"));

        AdSummary draft = adService.createDraft(request, user);
        return new ResponseEntity<>(draft, null, HttpStatus.CREATED);
    }

    @PostMapping(value = "/api/ads/{id}", produces = "application/json")
    public ResponseEntity<AdSummary> update(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @Valid @RequestBody AdRequestDto request
    ) throws AppException {
        Long userId = authService.parseToken(token).getId();

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AppException("Ad not found", HttpStatus.NOT_FOUND));

        if (!Objects.equals(ad.getUser().getId(), userId)) {
            throw new AppException("Ad belongs to another user", HttpStatus.FORBIDDEN);
        }

        AdSummary updated = adService.updateAd(request, ad);
        return new ResponseEntity<>(updated, null, HttpStatus.OK);
    }

    @GetMapping(value = "/api/ads/crawl", produces = "application/json")
    public ResponseEntity<List<String>> crawlUrls(
            @RequestHeader("Authorization") String token
    ) throws AppException {
        if (token == null || !token.equals("CRAWLER")) {
            throw new AppAuthException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        List<String> urls = adRepository.getAllIds().stream()
                .map(id -> "/" + id)
                .collect(Collectors.toList());

        return new ResponseEntity<>(urls, null, HttpStatus.OK);
    }

}
