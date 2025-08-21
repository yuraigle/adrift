package ru.orlov.adrift.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    @Query("""
            select t from Template t
                left join fetch t.questions q
                left join fetch q.options
            where t.id = :id
            """)
    Template getTemplateWithQuestions(Long id);

}