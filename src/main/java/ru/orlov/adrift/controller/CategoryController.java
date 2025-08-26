package ru.orlov.adrift.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.orlov.adrift.domain.CategoryRepository;
import ru.orlov.adrift.domain.CategorySummary;
import ru.orlov.adrift.domain.ex.AppException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping(value = "/api/categories", produces = "application/json")
    public List<CategorySummary> list() throws AppException {
        return categoryRepository.findAllSummary();
    }

}
