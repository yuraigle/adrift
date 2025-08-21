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

    private static final List<Question.Type> typesField = List.of(
            Question.Type.NUMBER,
            Question.Type.DECIMAL,
            Question.Type.TEXT
    );

    private static final List<Question.Type> typesOption = List.of(
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

        fillAdFields(ad, req.getFields());

        Ad savedAd = adRepository.save(ad);

        return adRepository.findAdSummaryById(savedAd.getId())
                .orElseThrow(() -> new AppException("Ad not created"));
    }

    @Transactional
    public AdSummary updateAd(AdRequestDto req, Ad ad) throws AppException {
        Category category = categoryRepository.findById(req.getCategory())
                .orElseThrow(() -> new AppException("Category not found", 400));

        ad.setCategory(category);
        ad.setTitle(req.getTitle());
        ad.setDescription(req.getDescription());
        ad.setPrice(req.getPrice());
        ad.setUpdated(LocalDateTime.now());

        fillAdFields(ad, req.getFields());

        adRepository.save(ad);

        return adRepository.findAdSummaryById(ad.getId())
                .orElseThrow(() -> new AppException("Something went wrong"));
    }

    private void fillAdFields(Ad ad, List<AdRequestDto.AdFieldDto> fields) throws AppException {
        ad.getFields().clear();
        ad.getOptions().clear();

        for (Question question : ad.getCategory().getTemplate().getQuestions()) {
            List<AdRequestDto.AdFieldDto> answers = fields.stream()
                    .filter(f -> question.getId().equals(f.getQid()))
                    .toList();

            boolean hasAnswer = !answers.isEmpty() &&
                    answers.stream().anyMatch(a -> a.getValue() != null && !a.getValue().isEmpty());
            if (question.getRequired() && !hasAnswer) {
                throw new AppException("Missing required field: " + question.getName(), 400);
            }

            if (answers.isEmpty()) {
                continue; // no answers for this optional question
            }

            if (typesField.contains(question.getType())) {
                if (answers.size() > 1) {
                    throw new AppException("Multiple answers for field: " + question.getName(), 400);
                }

                String value = answers.getFirst().getValue();
                validateFieldValue(question, value);
                createAdField(ad, question, value);
            } else if (typesOption.contains(question.getType())) {
                if (question.getType() == Question.Type.OPTION && answers.size() > 1) {
                    throw new AppException("Multiple answers for field: " + question.getName(), 400);
                }

                for (AdRequestDto.AdFieldDto answer : answers) {
                    if (answer.getValue() != null && !answer.getValue().isEmpty()) {
                        createAdOption(ad, question, answer.getValue());
                    }
                }
            } else {
                throw new AppException("Unknown question type", 400);
            }
        }
    }

    private void validateFieldValue(Question question, String value) throws AppException {
        if (value == null || value.isEmpty()) {
            return;
        }

        String message = question.getMessage() == null
                ? "Invalid value for field #" + question.getId()
                : question.getMessage().trim();

        BigDecimal decVal = null;
        Integer intVal = null;

        try {
            if (question.getType() == Question.Type.NUMBER) {
                intVal = Integer.parseInt(value.trim());
            } else if (question.getType() == Question.Type.DECIMAL) {
                decVal = new BigDecimal(value.trim());
            }
        } catch (NumberFormatException e) {
            throw new AppException(message, 400);
        }

        if (question.getMin() != null) {
            if (question.getType() == Question.Type.NUMBER && intVal < question.getMin()) {
                throw new AppException(message, 400);
            } else if (question.getType() == Question.Type.DECIMAL
                    && decVal.compareTo(new BigDecimal(question.getMin())) < 0
            ) {
                throw new AppException(message, 400);
            }
        }

        if (question.getMax() != null) {
            if (question.getType() == Question.Type.NUMBER && intVal > question.getMax()) {
                throw new AppException(message, 400);
            } else if (question.getType() == Question.Type.DECIMAL
                    && decVal.compareTo(new BigDecimal(question.getMax())) > 0
            ) {
                throw new AppException(message, 400);
            }
        }

        if (question.getRegex() != null && !question.getRegex().isEmpty()) {
            if (!value.matches(question.getRegex())) {
                throw new AppException(message, 400);
            }
        }
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

}
