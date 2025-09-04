package ru.orlov.adrift.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.orlov.adrift.controller.dto.TemplateDto;
import ru.orlov.adrift.domain.*;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.service.AdSearchService;

import java.util.List;

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

        TemplateDto dto = new TemplateDto();
        Template template = templateRepository.getTemplateWithQuestions(category.getId());

        dto.setId(template.getId());

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
            @PageableDefault(size = 12) Pageable pageable
    ) throws AppException {
        return adSearchService.listByCategory(id, pageable);
    }

}
