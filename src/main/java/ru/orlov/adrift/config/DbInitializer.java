package ru.orlov.adrift.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.asm.TypeReference;
import org.springframework.context.annotation.Configuration;
import ru.orlov.adrift.domain.*;

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
    private final TemplateRepository templateRepository;
    private final AdRepository adRepository;
    private final ObjectMapper objectMapper;

    public DbInitializer(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            QuestionRepository questionRepository,
            TemplateRepository templateRepository,
            AdRepository adRepository,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
        this.templateRepository = templateRepository;
        this.adRepository = adRepository;
        this.objectMapper = objectMapper;

        this.categoryRepository.deleteAll();
        this.questionRepository.deleteAll();
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

            questionRepository.saveAll(yaml.questions);
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
        List<Ad> generatedAds = new ArrayList<>();
        for (Category cat : categoryRepository.findAll()) {
            for (int i = 0; i < 5; i++) {
                Ad ad = new Ad();
                ad.setUser(user);
                ad.setCategory(cat);
                ad.setTitle(faker.address().fullAddress());
                ad.setDescription(faker.lorem().paragraph());

                BigDecimal price = ad.getCategory().getSlug().contains("rent") ?
                        randomHousingRentPrice() : randomHousingSellPrice();
                ad.setPrice(price);

                ad.getFields().clear();

                Question qArea = questionRepository.findById(1L).orElse(null);
                if (qArea == null) {
                    log.error("Question 1 not found");
                    continue;
                }

                AdField field = new AdField();
                field.setAd(ad);
                field.setQuestion(qArea);
                field.setValDecimal(BigDecimal.valueOf(67.5));
                ad.getFields().add(field);

                ad.setCreated(LocalDateTime.now());
                generatedAds.add(ad);
            }
        }

        adRepository.saveAll(generatedAds);
        log.info("{} ads created", generatedAds.size());
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
        public List<CategoryYamlRef> categories;

        @Data
        private static class CategoryYamlRef {
            private Long id;
            private String name;
            private String slug;
            private Long template;
        }
    }

    @Data
    private static class QuestionsResource {
        private List<Question> questions;
    }

    @Data
    private static class TemplatesResource {
        private List<TemplateYamlRef> templates;

        @Data
        private static class TemplateYamlRef {
            private Long id;
            private String name;
            private List<Long> questions;
        }
    }
}
