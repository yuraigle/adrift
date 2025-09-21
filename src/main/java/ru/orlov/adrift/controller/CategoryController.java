package ru.orlov.adrift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.orlov.adrift.controller.dto.SearchFilterDto;
import ru.orlov.adrift.controller.dto.TemplateDto;
import ru.orlov.adrift.domain.*;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.service.AdSearchService;

import java.util.Base64;
import java.util.List;
import java.util.Set;

@Log4j2
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;
    private final TemplateRepository templateRepository;
    private final AdSearchService adSearchService;

    @GetMapping(value = "/api/categories", produces = "application/json")
    public List<CategorySummary> listAll() throws AppException {
        return categoryRepository.findAllSummary();
    }

    @GetMapping(value = "/api/categories/slug/{slug}", produces = "application/json")
    public CategorySummary getBySlug(
            @PathVariable String slug
    ) throws AppException {
        return categoryRepository.findSummaryBySlug(slug)
                .orElseThrow(() -> new AppException("Category not found"));
    }

    @GetMapping(value = "/api/categories/{id}", produces = "application/json")
    public CategorySummary getById(
            @PathVariable Long id
    ) throws AppException {
        return categoryRepository.findSummaryById(id)
                .orElseThrow(() -> new AppException("Category not found"));
    }

    @GetMapping(value = "/api/categories/{id}/template", produces = "application/json")
    public TemplateDto getTemplate(
            @PathVariable Long id
    ) throws AppException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException("Category not found"));

        Long tid = category.getTemplate().getId();
        Template template = templateRepository.getTemplateWithQuestions(tid);

        TemplateDto dto = new TemplateDto();
        dto.setId(tid);

        for (Question question : template.getQuestions()) {
            dto.getQuestions().add(new TemplateDto.QuestionDto(question));

            for (Option option : question.getOptions()) {
                dto.getOptions().put(option.getId(), option.getName());
            }
        }

        return dto;
    }

    @GetMapping(value = "/api/categories/{id}/a", produces = "application/json")
    public Page<AdSummary> listAds(
            @PathVariable Long id,
            @PageableDefault(size = 12) Pageable pageable,
            @RequestParam(value = "filter", required = false) String filterEnc
    ) throws AppException {
        SearchFilterDto filter = getSearchFilterDto(filterEnc);

        log.info("Filter: {}", filter);

        return adSearchService.listByCategory(id, filter, pageable);
    }

    private SearchFilterDto getSearchFilterDto(String filterEnc) throws AppException {
        if (filterEnc == null || filterEnc.isBlank()) {
            return null;
        }

        SearchFilterDto filterDto;

        // parse filter
        try {
            String json = new String(Base64.getDecoder().decode(filterEnc));
            ObjectMapper objectMapper = new ObjectMapper();
            filterDto = objectMapper.readValue(json, SearchFilterDto.class);
        } catch (Exception e) {
            throw new AppException("Invalid filter request");
        }

        // validate filter object
        try (
                ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        ) {
            Set<ConstraintViolation<SearchFilterDto>> violations =
                    factory.getValidator().validate(filterDto);

            if (!violations.isEmpty()) {
                ConstraintViolation<SearchFilterDto> error =
                        violations.stream().findFirst().get();
                throw new AppException(error.getMessage());
            }
        }

        return filterDto;
    }

}
