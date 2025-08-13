package ru.orlov.adrift.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "templates")
public class Template {

    @Id
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "templates_questions",
            joinColumns = @JoinColumn(name = "template_id"),
            inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    @OrderColumn(name = "ord")
    private List<Question> questions = new ArrayList<>();
}
