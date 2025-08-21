package ru.orlov.adrift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.orlov.adrift.domain.AdRepository;
import ru.orlov.adrift.initializr.FakeAdLoader;
import ru.orlov.adrift.initializr.FakeUserLoader;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InitializerTests extends AbstractTest {

    @Autowired
    FakeAdLoader fakeAdLoader;

    @Autowired
    FakeUserLoader fakeUserLoader;

    @Autowired
    AdRepository adRepository;

    @Test
    void fakeAdsAreCreated() {
        cleanupTestAds();

        long cntBefore = adRepository.count();
        fakeAdLoader.createFakeAds(1);
        long cntAfter = adRepository.count();

        assert cntAfter > cntBefore;

        fakeAdLoader.deleteFakeAds();
    }

    @Test
    void fakeUsersAreCreated() {
        fakeUserLoader.deleteFakeUsers();

        long cntBefore = userRepository.count();
        fakeUserLoader.createFakeUsers(1);
        long cntAfter = userRepository.count();

        assert cntAfter > cntBefore;

        fakeUserLoader.deleteFakeUsers();
    }

}
