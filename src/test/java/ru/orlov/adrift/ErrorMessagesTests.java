package ru.orlov.adrift;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ErrorMessagesTests extends AbstractTest {

    @Test
    void invalidApiUrlReturns404AndJson() {
        ResponseEntity<String> response = apiRequestGet("/api/invalid");

        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
        assert response.getBody() != null;
        assert response.getBody().contains("\"messages\":[");
    }

    @Test
    void invalidPublicUrlReturns404AndHtml() {
        ResponseEntity<String> response = apiRequestGet("/invalid");

        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
        assert response.getBody() != null;
        assert response.getBody().toLowerCase().contains("<html");
    }

    @Test
    void emptyPostBodyReturnsArrayOfMessages() {
        ResponseEntity<String> response = apiRequestPost("/api/auth/login", null, null, String.class);

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getBody() != null;
        assert response.getBody().contains("\"messages\":[");
    }

}
