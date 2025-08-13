package ru.orlov.adrift.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "questions")
public class Question {

    @Id
    private Long id;

    private String name;

    @Column(name = "type")
    @Convert(converter = QuestionTypeConverter.class)
    private Type type;

    private Boolean required;

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        TEXT("TEXT"),
        NUMBER("NUMBER"),
        DECIMAL("DECIMAL");

        private final String value;
    }

    @Converter
    public static class QuestionTypeConverter implements AttributeConverter<Type, String> {

        @Override
        public String convertToDatabaseColumn(Type type) {
            return type != null ? type.getValue() : null;
        }

        @Override
        public Type convertToEntityAttribute(String dbData) {
            for (Type type : Type.values()) {
                if (type.getValue().equals(dbData)) return type;
            }
            throw new IllegalArgumentException("Unknown type: " + dbData);
        }
    }
}
