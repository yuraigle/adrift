package ru.orlov.adrift.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.orlov.adrift.controller.dto.AdRequestDto;
import ru.orlov.adrift.domain.*;
import ru.orlov.adrift.domain.ex.AppException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public AdSummary createDraft(AdRequestDto req, User user) throws AppException {
        Category category = categoryRepository.findById(req.getCategory())
                .orElseThrow(() -> new AppException("Category not found", 400));

        Ad ad = new Ad();
        ad.setCategory(category);
        ad.setUser(user);

        ad.setTitle(req.getTitle());
        ad.setDescription(req.getDescription());
        ad.setPrice(req.getPrice());
        ad.setCreated(LocalDateTime.now());
        ad.getFields().clear();

        for (Question question : category.getTemplate().getQuestions()) {
            List<AdRequestDto.AdFieldDto> answers = req.getFields().stream()
                    .filter(f -> question.getId().equals(f.getQid()))
                    .toList();

            if (answers.isEmpty()) {
                continue;  // no answers
            }

            AdField field = new AdField();
            field.setAd(ad);
            field.setQuestion(question);
            ad.getFields().add(field);

            for (AdRequestDto.AdFieldDto ans : answers) {
                if (question.getType() == Question.Type.NUMBER) {
                    try {
                        field.setValNumber(Integer.parseInt(ans.getValue()));
                    } catch (NumberFormatException ignore) {
                    }
                } else if (question.getType() == Question.Type.DECIMAL) {
                    try {
                        field.setValDecimal(new BigDecimal(ans.getValue()));
                    } catch (NumberFormatException ignore) {
                    }
                } else if (question.getType() == Question.Type.TEXT) {
                    field.setValText(ans.getValue());
                }
            }
        }

        Ad savedAd = adRepository.save(ad);

        return adRepository.findAdSummaryById(savedAd.getId())
                .orElseThrow(() -> new AppException("Ad not created"));
    }

    public void addImages() {

    }

    public void activateDraft() {

    }

}
