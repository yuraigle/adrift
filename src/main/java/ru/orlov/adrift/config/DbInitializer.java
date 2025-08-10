package ru.orlov.adrift.config;

import org.springframework.stereotype.Component;
import ru.orlov.adrift.domain.User;
import ru.orlov.adrift.domain.UserRepository;

import java.time.LocalDateTime;

@Component
public class DbInitializer {

    private final UserRepository userRepository;

    public DbInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;

        if (this.userRepository.count() == 0) {
            initUsersTable();
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
}
