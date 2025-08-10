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
        log.info("Admin user created");
    }

    private void initCategoriesTable() {
        try (
                InputStream is = TypeReference.class
                        .getResourceAsStream("/data/categories.yaml")
        ) {
            CategoriesResource yaml = objectMapper
                    .readValue(is, CategoriesResource.class);

            categoryRepository.saveAll(yaml.categories);
            log.info("{} categories created", yaml.categories.size());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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

                ad.setWww(faker.internet().url());
                ad.setContact(faker.phoneNumber().cellPhone());
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
        public List<Category> categories;
    }
}
