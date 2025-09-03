package ru.orlov.adrift;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import ru.orlov.adrift.controller.dto.LoginRequestDto;
import ru.orlov.adrift.controller.dto.LoginResponseDto;
import ru.orlov.adrift.domain.*;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.initializr.CategoryLoader;
import ru.orlov.adrift.initializr.FakeAdLoader;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Log4j2
@Sql({"/schema-sqlite.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FakeAdLoader fakeAdLoader;

    @Autowired
    CategoryLoader categoryLoader;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @BeforeEach
    void beforeEach() {
        createAdminUser();
        createTesterUser();

        if (categoryRepository.count() == 0) {
            log.info("Initializing categories...");
            categoryLoader.initQuestionsTable("/data-estates/questions.yaml");
            categoryLoader.initTemplatesTable("/data-estates/templates.yaml");
            categoryLoader.initCategoriesTable("/data-estates/categories.yaml");
        }
    }

    ResponseEntity<String> apiRequestGet(String path) {
        return apiRequestGet(path, null, String.class);
    }

    <T> ResponseEntity<T> apiRequestGet(String path, String token, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        if (token != null) {
            headers.add("Authorization", "Bearer " + token);
        }

        return restTemplate.exchange(
                url(path),
                HttpMethod.GET,
                new HttpEntity<>(null, headers),
                responseType
        );
    }

    <T> ResponseEntity<T> apiRequestPost(
            String path,
            Object form,
            String token,
            Class<T> responseType
    ) {
        String formBody;
        try {
            formBody = (new ObjectMapper()).writeValueAsString(form);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        if (token != null) {
            headers.add("Authorization", "Bearer " + token);
        }

        return restTemplate.exchange(
                url(path),
                HttpMethod.POST,
                new HttpEntity<>(formBody, headers),
                responseType
        );
    }

    void createAdminUser() {
        User user = userRepository.findByUsername("admin").orElse(new User());
        user.setEmail("admin@admin");
        user.setUsername("admin");
        user.setPassword(User.hashPassword("admin"));
        user.setCreated(LocalDateTime.now());
        this.userRepository.save(user);
    }

    void createTesterUser() {
        User user = userRepository.findByUsername("tester").orElse(new User());
        user.setEmail("tester@tester");
        user.setUsername("tester");
        user.setPassword(User.hashPassword("tester"));
        user.setCreated(LocalDateTime.now());
        this.userRepository.save(user);
    }

    String retrieveAdminToken() {
        LoginRequestDto req = new LoginRequestDto("admin@admin", "admin");
        ResponseEntity<LoginResponseDto> response = apiRequestPost(
                "/api/auth/login", req, null, LoginResponseDto.class
        );

        return response.getBody() != null ? response.getBody().getToken() : null;
    }

    String retrieveTesterToken() {
        LoginRequestDto req = new LoginRequestDto("tester@tester", "tester");
        ResponseEntity<LoginResponseDto> response = apiRequestPost(
                "/api/auth/login", req, null, LoginResponseDto.class
        );

        return response.getBody() != null ? response.getBody().getToken() : null;
    }

    Long generateRandomTestAd() throws AppException {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new AppException("No categories in DB", 500);
        }

        User user = userRepository.findByUsername("admin")
                .orElseThrow(() -> new AppException("No admin user in DB", 500));

        Random random = new Random();
        Category cat = categories.get(random.nextInt(categories.size()));

        return fakeAdLoader.generateFakeAd(cat, user);
    }

    void cleanupTestAds() {
        fakeAdLoader.deleteFakeAds();
    }

}
