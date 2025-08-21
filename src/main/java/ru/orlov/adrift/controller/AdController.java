package ru.orlov.adrift.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.orlov.adrift.controller.dto.AdRequestDto;
import ru.orlov.adrift.domain.*;
import ru.orlov.adrift.domain.ex.AppAuthException;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.initializr.FakeAdLoader;
import ru.orlov.adrift.service.AdService;
import ru.orlov.adrift.service.AuthService;

import java.util.Objects;

@Log4j2
@RestController
@RequiredArgsConstructor
public class AdController {

    private final AdRepository adRepository;
    private final AdService adService;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final FakeAdLoader fakeAdLoader;

    @GetMapping(value = "/api/ads/{id}", produces = "application/json")
    public AdSummary show(@PathVariable Long id) throws AppException {
        return adRepository.findAdSummaryById(id)
                .orElseThrow(() -> new AppException("Ad not found", 404));
    }

    // to handle SSR
    @GetMapping("/a/{id}")
    public ModelAndView index(@PathVariable Long id) {
//        return new ModelAndView("forward:/200.html");
        return new ModelAndView("forward:/a/" + id + "/index.html");
    }

    @PostMapping("/api/ads")
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

    @PostMapping("/api/ads/{id}")
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

    @GetMapping("/api/new-ad")
    public ResponseEntity<String> test() throws AppException {
        Category cat = categoryRepository.findById(1L)
                .orElseThrow(() -> new AppException("Category not found", 400));
        User user = userRepository.findByUsername("admin")
                .orElseThrow(() -> new AppException("User not found", 400));

        fakeAdLoader.generateFakeAd(cat, user);
        return new ResponseEntity<>("OK", null, HttpStatus.OK);
    }
}
