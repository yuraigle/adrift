package ru.orlov.adrift.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.orlov.adrift.controller.dto.AdRequestDto;
import ru.orlov.adrift.domain.AdRepository;
import ru.orlov.adrift.domain.AdSummary;
import ru.orlov.adrift.domain.User;
import ru.orlov.adrift.domain.UserRepository;
import ru.orlov.adrift.domain.ex.AppAuthException;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.service.AdService;
import ru.orlov.adrift.service.AuthService;

@Log4j2
@RestController
@RequiredArgsConstructor
public class AdController {

    private final AdRepository adRepository;
    private final AdService adService;
    private final AuthService authService;
    private final UserRepository userRepository;

    @GetMapping(value = "/api/ads/{id}", produces = "application/json")
    public AdSummary show(@PathVariable Long id) throws AppException {
        return adRepository.findAdSummaryById(id)
                .orElseThrow(() -> new AppException("Ad not found", 404));
    }

    // to handle SSR
    @GetMapping("/a/{id}")
    public ModelAndView index(@PathVariable Long id) {
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

}
