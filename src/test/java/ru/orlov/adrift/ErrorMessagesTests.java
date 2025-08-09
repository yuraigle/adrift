package ru.orlov.adrift;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ErrorMessagesTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void invalidUrlReturns404() {
        ResponseEntity<String> response = restTemplate
                .getForEntity(url("/non-existent-url"), String.class);

        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    void invalidRequestBodyReturns400() {
        Map<String, String> req = Map.of("username", "");

        ResponseEntity<String> response = restTemplate
                .postForEntity(url("/api/auth/login"), req, String.class);

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
    }

    @Test
    void invalidRequestBodyReturnsArrayOfMessages() {
        Map<String, String> req = Map.of("password", "");

        ResponseEntity<String> response = restTemplate
                .postForEntity(url("/api/auth/login"), req, String.class);

        String body = response.getBody();

        assert body != null;
        assert body.contains("\"messages\":[");
        assert body.contains("password");
    }

}
