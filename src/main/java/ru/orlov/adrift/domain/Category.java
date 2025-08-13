package ru.orlov.adrift.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {

    @Id
    private Long id;

    private String name;
    private String slug;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private Template template;

}
