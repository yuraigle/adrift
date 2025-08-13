package ru.orlov.adrift.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.orlov.adrift.controller.dto.AdRequestDto;
import ru.orlov.adrift.domain.*;
import ru.orlov.adrift.domain.ex.AppException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final CategoryRepository categoryRepository;

    public AdSummary createDraft(AdRequestDto request, User user) throws AppException {
        Category category = categoryRepository.findById(request.getCategory())
                .orElseThrow(() -> new AppException("Category not found", 400));

        Ad ad = new Ad();
        ad.setCategory(category);
        ad.setUser(user);

        ad.setTitle(request.getTitle());
        ad.setDescription(request.getDescription());
        ad.setPrice(request.getPrice());
        ad.setCreated(LocalDateTime.now());

        Ad savedAd = adRepository.save(ad);

        return adRepository.findAdSummaryById(savedAd.getId())
                .orElseThrow(() -> new AppException("Ad not created"));
    }

    public void addImages() {

    }

    public void activateDraft() {

    }

}
