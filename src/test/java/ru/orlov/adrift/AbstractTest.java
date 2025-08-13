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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

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

    String retrieveToken() {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("admin");
        request.setPassword("admin");

        ResponseEntity<LoginResponseDto> response = apiRequestPost(
                "/api/auth/login", request, null, LoginResponseDto.class
        );

        return response.getBody() != null ? response.getBody().getToken() : null;
    }

}
