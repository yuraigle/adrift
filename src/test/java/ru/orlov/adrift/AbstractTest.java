package ru.orlov.adrift;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import ru.orlov.adrift.controller.dto.LoginRequestDto;
import ru.orlov.adrift.controller.dto.LoginResponseDto;
import ru.orlov.adrift.domain.*;
import ru.orlov.adrift.domain.ex.AppException;
import ru.orlov.adrift.initializr.FakeAdLoader;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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

    private String url(String path) {
        return "http://localhost:" + port + path;
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

    String retrieveAdminToken() {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("admin");
        request.setPassword("admin");

        ResponseEntity<LoginResponseDto> response = apiRequestPost(
                "/api/auth/login", request, null, LoginResponseDto.class
        );

        return response.getBody() != null ? response.getBody().getToken() : null;
    }

    String retrieveTesterToken() {
        // create tester
        User user = userRepository.findByUsername("tester").orElse(new User());
        user.setEmail("tester@tester");
        user.setUsername("tester");
        user.setPassword(User.hashPassword("tester"));
        user.setCreated(LocalDateTime.now());
        this.userRepository.save(user);

        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("tester");
        request.setPassword("tester");

        ResponseEntity<LoginResponseDto> response = apiRequestPost(
                "/api/auth/login", request, null, LoginResponseDto.class
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
