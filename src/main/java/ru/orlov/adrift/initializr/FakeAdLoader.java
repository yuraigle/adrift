package ru.orlov.adrift.initializr;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import ru.orlov.adrift.controller.dto.AdRequestDto;
import ru.orlov.adrift.domain.*;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.service.AdService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Some fake Ads for testing purposes
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class FakeAdLoader {

    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;
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
            log.error("No users in DB");
            return;
        }

        int cntCreated = 0;
        Random random = new Random();
        for (Category cat : categoryRepository.findAll()) {
            int cnt = numPerCategory;
            if (cat.getId() == 1L && numPerCategory > 1) { // tests: 1 per cat
                cnt = 100;
            }

            for (int i = 0; i < cnt; i++) {
                try {
                    int usrN = random.nextInt(users.size());
                    generateFakeAd(cat, users.get(usrN));
                    cntCreated++;
                } catch (AppException e) {
                    log.error(e.getMessage());
                }
            }
        }

        log.info("{} fake Ads created", cntCreated);
    }

    public Long generateFakeAd(Category cat, User user) throws AppException {
        Faker faker = new Faker(Locale.US);
        AdRequestDto req = new AdRequestDto();

        String title = "Test AD #" + faker.number().numberBetween(100, 999);
        title += " " + faker.address().fullAddress();
        req.setTitle(StringUtils.left(title, 255));

        req.setDescription(faker.hitchhikersGuideToTheGalaxy().quote());
        req.setCategory(cat.getId());

        BigDecimal price = cat.getName().toLowerCase().contains("rent") ?
                randomHousingRentPrice() : randomHousingSellPrice();
        req.setPrice(price);

        Template template = templateRepository.getTemplateWithQuestions(cat.getTemplate().getId());
        for (Question q : template.getQuestions()) {
            if (q.getName().equalsIgnoreCase("Area")) {
                double area = faker.number().randomDouble(2, 40, 200);
                req.getFields().add(new AdRequestDto.AdFieldDto(q.getId(), Double.toString(area)));
            } else if (q.getName().equalsIgnoreCase("Construction Year")) {
                int yr = faker.random().nextInt(1850, 2020);
                req.getFields().add(new AdRequestDto.AdFieldDto(q.getId(), Integer.toString(yr)));
            } else if (q.getName().equalsIgnoreCase("WWW")) {
                String www = "https://" + faker.internet().url();
                req.getFields().add(new AdRequestDto.AdFieldDto(q.getId(), www));
            }

            if (q.getType() == Question.Type.OPTION) {
                int optN = faker.random().nextInt(0, q.getOptions().size() - 1);
                String optId = q.getOptions().get(optN).getId().toString();
                req.getFields().add(new AdRequestDto.AdFieldDto(q.getId(), optId));
            }

            if (q.getType() == Question.Type.CHECKBOX) {
                q.getOptions().forEach(opt -> {
                    if (faker.random().nextBoolean()) {
                        String optId = opt.getId().toString();
                        req.getFields().add(new AdRequestDto.AdFieldDto(q.getId(), optId));
                    }
                });
            }
        }

        return adService.createDraft(req, user).getId();
    }

    @Modifying
    @Transactional
    public void deleteFakeAds() {
        List<Ad> ads = adRepository.findAllByTitleStartingWith("Test AD ");
        adRepository.deleteAll(ads);
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
