package ru.orlov.adrift.initializr;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.orlov.adrift.domain.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class InitializerService {

    private final CategoryLoader categoryLoader;
    private final FakeUserLoader userLoader;
    private final FakeAdLoader adLoader;

    @Value("${app.clear-on-startup:false}")
    private Boolean clearOnStartup;

    private final AdRepository adRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;
    private final QuestionRepository questionRepository;
    private final TemplateRepository templateRepository;

    private void clearDatabase() {
        adRepository.deleteAll();
        userRepository.deleteAll();
        categoryRepository.deleteAll();
        optionRepository.deleteAll();
        questionRepository.deleteAll();
        templateRepository.deleteAll();
    }

    @Transactional
    @PostConstruct
    void init() {
        if (clearOnStartup) {
            clearDatabase();
        }

        if (categoryLoader.isCategoriesTableEmpty()) {
            log.info("Initializing Template tables ...");
            categoryLoader.initCategoryTables();
        }

        if (userLoader.isUsersTableEmpty()) {
            log.info("Initializing Users table ...");
            userLoader.createAdminUser();
            userLoader.createFakeUsers(10);
        }

        if (adLoader.isAdsTableEmpty()) {
            log.info("Initializing Ads table ...");
            adLoader.createFakeAds(5);
        }

    }
}
