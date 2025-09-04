package ru.orlov.adrift.controller.dto;

import lombok.Data;
import ru.orlov.adrift.domain.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TemplateDto {

    private Long id;

    private List<QuestionDto> questions = new ArrayList<>();

    private Map<Long, String> options = new HashMap<>();

    @Data
    public static class QuestionDto {
        private Long id;
        private String name;
        private Question.Type type;

        public QuestionDto(Question q) {
            this.id = q.getId();
            this.name = q.getName();
            this.type = q.getType();
        }
    }

}
