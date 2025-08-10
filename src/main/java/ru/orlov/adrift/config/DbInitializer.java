package ru.orlov.adrift.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.asm.TypeReference;
import org.springframework.stereotype.Component;
import ru.orlov.adrift.domain.Category;
import ru.orlov.adrift.domain.CategoryRepository;
import ru.orlov.adrift.domain.User;
import ru.orlov.adrift.domain.UserRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

@Log4j2
@Component
public class DbInitializer {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ObjectMapper objectMapper;

    public DbInitializer(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;

        if (this.userRepository.count() == 0) {
            initUsersTable();
        }

        if (this.categoryRepository.count() == 0) {
            initCategoriesTable();
        }
    }

    private void initUsersTable() {
        User user = new User();
        user.setEmail("admin@admin");
        user.setUsername("admin");
        user.setPassword(User.hashPassword("admin"));
        user.setCreated(LocalDateTime.now());
        this.userRepository.save(user);
    }

    private void initCategoriesTable() {
        try (
                InputStream is = TypeReference.class
                        .getResourceAsStream("/data/categories.yaml")
        ) {
            CategoriesResource yaml = objectMapper
                    .readValue(is, CategoriesResource.class);

            categoryRepository.saveAll(yaml.categories);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Data
    private static class CategoriesResource {
        public List<Category> categories;
    }
}
