package ru.orlov.adrift;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static ru.orlov.adrift.controller.AuthController.LoginResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    ResponseEntity<String> testRequestGet(String path) {
        return restTemplate.getForEntity(url(path), String.class);
    }

    ResponseEntity<String> testRequestPost(String path, Map<String, String> form, String token) {
        String encodedFormBody = JSONObject.wrap(form).toString();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        if (token != null) {
            headers.add("Authorization", "Bearer " + token);
        }

        return restTemplate.exchange(
                url(path),
                HttpMethod.POST,
                new HttpEntity<>(encodedFormBody, headers),
                String.class
        );
    }

    ResponseEntity<String> testRequestPost(String path, Map<String, String> form) {
        return testRequestPost(path, form, null);
    }

    String retrieveToken(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json;charset=UTF-8");

        Map<String, String> form = Map.of(
                "username", username,
                "password", password
        );
        String encodedFormBody = JSONObject.wrap(form).toString();

        ResponseEntity<LoginResponseDto> response = restTemplate.exchange(
                url("/api/auth/login"),
                HttpMethod.POST,
                new HttpEntity<>(encodedFormBody, headers),
                LoginResponseDto.class
        );

        return response.getBody() != null ? response.getBody().getToken() : null;
    }

}
