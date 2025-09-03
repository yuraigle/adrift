package ru.orlov.adrift.initializr;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.asm.TypeReference;
import org.springframework.stereotype.Service;
import ru.orlov.adrift.domain.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Initial categories, fields and options
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class CategoryLoader {

    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final TemplateRepository templateRepository;
    private final ObjectMapper objectMapper;

    public boolean isCategoriesTableEmpty() {
        return categoryRepository.count() == 0;
    }

    public void initCategoryTables() {
        initQuestionsTable("/data/questions.yaml");
        initTemplatesTable("/data/templates.yaml");
        initCategoriesTable("/data/categories.yaml");
    }

    public void initQuestionsTable(String filename) {
        int cntQuestions = 0;
        int cntOptions = 0;

        try (
                InputStream is = TypeReference.class
                        .getResourceAsStream(filename)
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
                    cntOptions++;
                }

                questionList.add(question);
                cntQuestions++;
            }

            questionRepository.saveAll(questionList);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        log.info("{} questions created", cntQuestions);
        log.info("{} options created", cntOptions);
    }

    public void initTemplatesTable(String filename) {
        int cntTemplates = 0;

        try (
                InputStream is = TypeReference.class
                        .getResourceAsStream(filename)
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
                cntTemplates++;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        log.info("{} templates created", cntTemplates);
    }

    public void initCategoriesTable(String filename) {
        int cntCategories = 0;

        try (
                InputStream is = TypeReference.class
                        .getResourceAsStream(filename)
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
                cntCategories++;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        log.info("{} categories created", cntCategories);
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
