package ru.orlov.adrift.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import ru.orlov.adrift.domain.AdRepository;
import ru.orlov.adrift.domain.AdSummary;
import ru.orlov.adrift.domain.ex.AppException;

@RestController
@RequiredArgsConstructor
public class AdController {

    private final AdRepository adRepository;

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

}
