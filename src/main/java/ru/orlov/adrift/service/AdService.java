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
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class AdService {

    private final AdRepository adRepository;
    private final CategoryRepository categoryRepository;
    private final OptionRepository optionRepository;

    private final List<Question.Type> typesField = List.of(
            Question.Type.NUMBER,
            Question.Type.DECIMAL,
            Question.Type.TEXT
    );

    private final List<Question.Type> typesOption = List.of(
            Question.Type.CHECKBOX,
            Question.Type.OPTION
    );

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
        ad.getOptions().clear();

        for (Question question : category.getTemplate().getQuestions()) {
            List<AdRequestDto.AdFieldDto> answers = req.getFields().stream()
                    .filter(f -> question.getId().equals(f.getQid()))
                    .toList();

            if (answers.isEmpty()) {
                continue;  // no answers for this question
            }

            // todo check for required questions
            // todo fields validation

            if (typesField.contains(question.getType())) {
                if (answers.size() > 1) {
                    throw new AppException("Multiple answers for field #" + question.getId(), 400);
                }

                createAdField(ad, question, answers.getFirst().getValue());
            } else if (typesOption.contains(question.getType())) {
                if (question.getType() == Question.Type.OPTION && answers.size() > 1) {
                    throw new AppException("Multiple answers for field #" + question.getId(), 400);
                }

                for (AdRequestDto.AdFieldDto answer : answers) {
                    createAdOption(ad, question, answer.getValue());
                }
            } else {
                throw new AppException("Unknown question type", 400);
            }
        }

        Ad savedAd = adRepository.save(ad);

        return adRepository.findAdSummaryById(savedAd.getId())
                .orElseThrow(() -> new AppException("Ad not created"));
    }

    private void createAdField(Ad ad, Question question, String value) throws AppException {
        AdField field = new AdField();
        field.setAd(ad);
        field.setQuestion(question);

        if (question.getType() == Question.Type.NUMBER) {
            try {
                field.setValNumber(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                throw new AppException("Invalid number value for field #" + question.getId(), 400);
            }
        } else if (question.getType() == Question.Type.DECIMAL) {
            try {
                field.setValDecimal(new BigDecimal(value));
            } catch (NumberFormatException e) {
                throw new AppException("Invalid decimal value for field #" + question.getId(), 400);
            }
        } else if (question.getType() == Question.Type.TEXT) {
            field.setValText(value == null ? "" : value.trim());
        }

        ad.getFields().add(field);
    }

    private void createAdOption(Ad ad, Question question, String value) throws AppException {
        if (value == null) {
            return;
        }

        long oid;

        try {
            oid = Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            throw new AppException("Invalid option for field #" + question.getId(), 400);
        }

        Option opt = optionRepository.findById(oid).orElseThrow(
                () -> new AppException("Invalid option for field #" + question.getId(), 400)
        );

        if (!Objects.equals(opt.getQuestion().getId(), question.getId())) {
            throw new AppException("Invalid option for field #" + question.getId(), 400);
        }

        AdOption option = new AdOption();
        option.setAd(ad);
        option.setQuestion(question);
        option.setOption(opt);
        ad.getOptions().add(option);
    }

    public void addImages() {

    }

    public void activateDraft() {

    }

}
