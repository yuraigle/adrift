package ru.orlov.adrift.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.asm.TypeReference;
import org.springframework.context.annotation.Configuration;
import ru.orlov.adrift.controller.dto.AdRequestDto;
import ru.orlov.adrift.domain.*;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.service.AdService;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Configuration
public class DbInitializer {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final TemplateRepository templateRepository;
    private final AdRepository adRepository;
    private final AdService adService;
    private final ObjectMapper objectMapper;

    public DbInitializer(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            QuestionRepository questionRepository,
            OptionRepository optionRepository,
            TemplateRepository templateRepository,
            AdRepository adRepository,
            AdService adService,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.templateRepository = templateRepository;
        this.adRepository = adRepository;
        this.adService = adService;
        this.objectMapper = objectMapper;

        this.adRepository.deleteAll();
        this.categoryRepository.deleteAll();
        this.questionRepository.deleteAll();
        this.optionRepository.deleteAll();
        this.templateRepository.deleteAll();

        if (this.categoryRepository.count() == 0) {
            initQuestionsTable();
            initTemplatesTable();
            initCategoriesTable();
        }

        if (this.userRepository.count() == 0) {
            initUsersTable();
        }

        if (this.adRepository.count() == 0) {
            initAdsTable();
        }
    }

    private void initQuestionsTable() {
        try (
                InputStream is = TypeReference.class
                        .getResourceAsStream("/data/questions.yaml")
        ) {
            QuestionsResource yaml = objectMapper
                    .readValue(is, QuestionsResource.class);

            List<Question> questionList = new ArrayList<>();
            for (QuestionsResource.QuestionYamlRef qq : yaml.questions) {
                Question question = new Question();
                question.setId(qq.getId());
                question.setName(qq.getName());
                question.setType(Question.Type.valueOf(qq.getType()));
                question.setRequired(qq.getRequired() != null && qq.getRequired());
                question.setMin(qq.getMin());
                question.setMax(qq.getMax());
                question.setRegex(qq.getRegex());
                question.setMessage(qq.getMessage());

                for (QuestionsResource.QuestionYamlRef.OptionYamlRef o1 : qq.getOptions()) {
                    Option option = new Option();
                    option.setId(o1.getId());
                    option.setName(o1.getName());
                    option.setQuestion(question);
                    question.getOptions().add(option);
                }

                questionList.add(question);
            }

            questionRepository.saveAll(questionList);
            log.info("{} questions created", yaml.questions.size());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void initTemplatesTable() {
        try (
                InputStream is = TypeReference.class
                        .getResourceAsStream("/data/templates.yaml")
        ) {
            TemplatesResource yaml = objectMapper
                    .readValue(is, TemplatesResource.class);

            for (TemplatesResource.TemplateYamlRef tpl : yaml.templates) {
                Template template = new Template();
                template.setId(tpl.getId());
                template.setName(tpl.getName());

                for (Long qId : tpl.getQuestions()) {
                    questionRepository.findById(qId)
                            .ifPresent(template.getQuestions()::add);
                }

                templateRepository.save(template);
            }

            log.info("Templates created");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void initCategoriesTable() {
        try (
                InputStream is = TypeReference.class
                        .getResourceAsStream("/data/categories.yaml")
        ) {
            CategoriesResource yaml = objectMapper
                    .readValue(is, CategoriesResource.class);

            for (CategoriesResource.CategoryYamlRef ref : yaml.categories) {
                Category category = new Category();
                category.setId(ref.getId());
                category.setName(ref.getName());
                category.setSlug(ref.getSlug());

                if (ref.getParent() != null) {
                    Category parent = categoryRepository
                            .findById(ref.getParent())
                            .orElseThrow();
                    category.setParent(parent);
                }

                templateRepository.findById(ref.getTemplate())
                        .ifPresent(category::setTemplate);

                categoryRepository.save(category);
            }
            log.info("Categories created");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void initUsersTable() {
        User user = new User();
        user.setEmail("admin@admin");
        user.setUsername("admin");
        user.setPassword(User.hashPassword("admin"));
        user.setCreated(LocalDateTime.now());
        this.userRepository.save(user);
        log.info("Admin user created");
    }

    private void initAdsTable() {
        User user = userRepository.findByUsername("admin").orElse(null);
        if (user == null) {
            log.info("Admin user not found: won't generate ads");
            return;
        }

        Faker faker = new Faker();
        try {
            for (Category cat : categoryRepository.findAll()) {
                for (int i = 0; i < 5; i++) {
                    AdRequestDto form = new AdRequestDto();
                    form.setCategory(cat.getId());
                    form.setTitle(faker.address().fullAddress());
                    form.setDescription(faker.lorem().paragraph());

                    BigDecimal price = cat.getSlug().contains("rent") ?
                            randomHousingRentPrice() : randomHousingSellPrice();
                    form.setPrice(price);

                    String www = "https://" + faker.internet().url();
                    Integer yr = faker.random().nextInt(1900, 2020);
                    Double area = faker.number().randomDouble(2, 40, 120);
                    form.setFields(List.of(
                            new AdRequestDto.AdFieldDto(1L, String.valueOf(area)),
                            new AdRequestDto.AdFieldDto(3L, yr.toString()),
                            new AdRequestDto.AdFieldDto(4L, www),
                            new AdRequestDto.AdFieldDto(6L, "4"), // pets allowed
                            new AdRequestDto.AdFieldDto(7L, "6"), // some features
                            new AdRequestDto.AdFieldDto(7L, "7")
                    ));

                    adService.createDraft(form, user);
                }
            }
        } catch (AppException e) {
            log.error(e.getMessage());
        }

        log.info("Ads created");
    }

    private BigDecimal randomHousingSellPrice() {
        int min = 40_000;
        int max = 300_000;
        double price = (new Faker()).number().randomDouble(2, min, max);
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal randomHousingRentPrice() {
        BigDecimal price = randomHousingSellPrice();
        return price.divide(BigDecimal.valueOf(150), 2, RoundingMode.HALF_UP);
    }

    @Data
    private static class CategoriesResource {
        public List<CategoryYamlRef> categories = new ArrayList<>();

        @Data
        private static class CategoryYamlRef {
            private Long id;
            private String name;
            private String slug;
            private Long template;
            private Long parent;
        }
    }

    @Data
    private static class QuestionsResource {
        private List<QuestionYamlRef> questions = new ArrayList<>();

        @Data
        private static class QuestionYamlRef {
            private Long id;
            private String name;
            private String type;
            private Boolean required;
            private Integer min;
            private Integer max;
            private String regex;
            private String message;
            private List<OptionYamlRef> options = new ArrayList<>();

            @Data
            private static class OptionYamlRef {
                private Long id;
                private String name;
            }
        }
    }

    @Data
    private static class TemplatesResource {
        private List<TemplateYamlRef> templates = new ArrayList<>();

        @Data
        private static class TemplateYamlRef {
            private Long id;
            private String name;
            private List<Long> questions;
        }
    }
}
