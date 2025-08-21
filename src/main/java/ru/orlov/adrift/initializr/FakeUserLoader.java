package ru.orlov.adrift.initializr;

import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import ru.orlov.adrift.domain.User;
import ru.orlov.adrift.domain.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Some fake Users for testing purposes
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class FakeUserLoader {

    private final UserRepository userRepository;

    public boolean isUsersTableEmpty() {
        return userRepository.count() == 0;
    }

    public void createAdminUser() {
        User user = new User();
        user.setEmail("admin@admin");
        user.setUsername("admin");
        user.setPassword(User.hashPassword("admin"));
        user.setCreated(LocalDateTime.now());

        this.userRepository.save(user);

        log.info("Admin user created");
    }

    public void createFakeUsers(int num) {
        Faker faker = new Faker(Locale.US);
        List<User> users = new ArrayList<>();

        for (int i = 0; i < num; i++) {
            User user = new User();
            user.setEmail("tester." + faker.internet().emailAddress());
            user.setUsername(faker.name().username());
            user.setPassword(User.hashPassword("password"));
            user.setCreated(LocalDateTime.now());

            users.add(user);
        }

        this.userRepository.saveAll(users);

        log.info("{} fake Users created", users.size());
    }

    @Modifying
    @Transactional
    public void deleteFakeUsers() {
        List<User> users = userRepository.findAllByEmailStartingWith("tester.");
        userRepository.deleteAll(users);
    }

}
