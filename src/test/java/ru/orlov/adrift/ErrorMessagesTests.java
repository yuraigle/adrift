package ru.orlov.adrift;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ErrorMessagesTests extends AbstractTest {

    @Test
    void invalidUrlReturns404() {
        ResponseEntity<String> response = testRequestGet("/api/invalid");

        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
    }

    @Test
    void emptyRequestBodyReturnsArrayOfMessages() {
        ResponseEntity<String> response = testRequestPost("/api/auth/login", null);

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getBody() != null;
        assert response.getBody().contains("\"messages\":[");
    }

    @Test
    void invalidRequestBodyReturns400() {
        Map<String, String> form = Map.of("username", "");
        ResponseEntity<String> response = testRequestPost("/api/auth/login", form);

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
    }

    @Test
    void invalidRequestBodyReturnsArrayOfMessages() {
        Map<String, String> form = Map.of("password", "");
        ResponseEntity<String> response = testRequestPost("/api/auth/login", form);

        assert response.getBody() != null;
        assert response.getBody().contains("\"messages\":[");
        assert response.getBody().contains("password");
    }

}
