package ru.orlov.adrift.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.orlov.adrift.controller.dto.AdDetailsDto;
import ru.orlov.adrift.controller.dto.AdRequestDto;
import ru.orlov.adrift.domain.*;
import ru.orlov.adrift.domain.ex.AppAuthException;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.service.AdService;
import ru.orlov.adrift.service.AuthService;
import ru.orlov.adrift.service.ImageService;

import java.util.List;
import java.util.Objects;

@Log4j2
@RestController
@RequiredArgsConstructor
public class AdController {

    private final AdService adService;
    private final AuthService authService;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final TemplateRepository templateRepository;

    @Transactional
    @GetMapping(value = "/api/ads/{id}", produces = "application/json")
    public AdDetailsDto show(@PathVariable Long id) throws AppException {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AppException("Ad not found", 404));

        AdDetailsDto dto = new AdDetailsDto();
        BeanUtils.copyProperties(ad, dto);
        adService.getAdFields(ad).forEach((qid, values) -> {
            for (String val : values) {
                dto.getFields().add(new AdDetailsDto.AdFieldDto(qid, val));
            }
        });

        return dto;
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
        Ad ad = findAdAndCheckOwner(id, token);

        AdSummary updated = adService.updateAd(request, ad);

        return new ResponseEntity<>(updated, null, HttpStatus.OK);
    }

    @PostMapping(value = "/api/ads/{id}/images", produces = "application/json")
    public ResponseEntity<String> upload(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws AppException {
        Ad ad = findAdAndCheckOwner(id, token);

        AdImage image = imageService.uploadAdImage(ad, file);

        return new ResponseEntity<>(image.getFilename(), null, HttpStatus.OK);
    }

    private Ad findAdAndCheckOwner(Long id, String token) throws AppException {
        Long userId = authService.parseToken(token).getId();

        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new AppException("Ad not found", HttpStatus.NOT_FOUND));

        if (!Objects.equals(ad.getUser().getId(), userId)) {
            throw new AppException("Ad belongs to another user", HttpStatus.FORBIDDEN);
        }

        return ad;
    }
}
