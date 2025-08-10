package ru.orlov.adrift.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.asm.TypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.orlov.adrift.domain.*;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class DbInitializer {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AdRepository adRepository;
    private final ObjectMapper objectMapper;

    public DbInitializer(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            AdRepository adRepository,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.adRepository = adRepository;
        this.objectMapper = objectMapper;

        if (this.userRepository.count() == 0) {
            initUsersTable();
        }

        if (this.categoryRepository.count() == 0) {
            initCategoriesTable();
        }

        if (this.adRepository.count() == 0) {
            initAdsTable();
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

    private void initAdsTable() {
        Faker faker = new Faker();

        List<Category> categories = categoryRepository.findAll();
        User user1 = userRepository.findAll(Pageable.ofSize(5))
                .getContent().getFirst();

        List<Ad> ads = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Ad ad = new Ad();
            ad.setUser(user1);
            ad.setCategory(categories.get(faker.random().nextInt(categories.size())));
            ad.setTitle(faker.address().fullAddress());
            ad.setDescription(String.join("\n", faker.lorem().sentences(5)));

            double price = faker.number().randomDouble(2, 40_000, 1000_000);
            if (ad.getCategory().getSlug().contains("rent")) {
                price = price / 150;
            }
            ad.setPrice(BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP));

            ad.setWww(faker.internet().url());
            ad.setContact(faker.phoneNumber().cellPhone());
            ad.setCreated(LocalDateTime.now());
            ads.add(ad);
        }

        adRepository.saveAll(ads);
    }

    @Data
    private static class CategoriesResource {
        public List<Category> categories;
    }
}
