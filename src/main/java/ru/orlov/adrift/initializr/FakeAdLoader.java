package ru.orlov.adrift.initializr;

import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.orlov.adrift.controller.dto.AdRequestDto;
import ru.orlov.adrift.domain.*;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.service.AdService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;

/**
 * Some fake Ads for testing purposes
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class FakeAdLoader {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AdRepository adRepository;
    private final AdService adService;

    public boolean isAdsTableEmpty() {
        return adRepository.count() == 0;
    }

    public void createFakeAds(int numPerCategory) {
        List<User> users = userRepository.findAll(Pageable.ofSize(5))
                .stream().toList();

        if (users.isEmpty()) {
            return;
        }

        int cntCreated = 0;
        Faker faker = new Faker(Locale.US);
        for (Category cat : categoryRepository.findAll()) {
            for (int i = 0; i < numPerCategory; i++) {
                AdRequestDto form = new AdRequestDto();
                form.setCategory(cat.getId());
                form.setTitle(faker.address().fullAddress());
                form.setDescription(faker.lorem().paragraph());

                BigDecimal price = cat.getSlug().contains("rent") ?
                        randomHousingRentPrice() : randomHousingSellPrice();
                form.setPrice(price);

                String www = "https://" + faker.internet().url();
                Integer yr = faker.random().nextInt(1850, 2020);
                Double area = faker.number().randomDouble(2, 40, 120);
                form.setFields(List.of(
                        new AdRequestDto.AdFieldDto(1L, String.valueOf(area)),
                        new AdRequestDto.AdFieldDto(3L, yr.toString()),
                        new AdRequestDto.AdFieldDto(4L, www),
                        new AdRequestDto.AdFieldDto(6L, "4"), // pets allowed
                        new AdRequestDto.AdFieldDto(7L, "6"), // some features
                        new AdRequestDto.AdFieldDto(7L, "7")
                ));

                int n = faker.random().nextInt(0, users.size() - 1);

                try {
                    adService.createDraft(form, users.get(n));
                    cntCreated++;
                } catch (AppException e) {
                    log.error(e.getMessage());
                }
            }
        }

        log.info("{} fake Ads created", cntCreated);
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

}
