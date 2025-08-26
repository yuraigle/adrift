package ru.orlov.adrift;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ErrorMessagesTests extends AbstractTest {

    @Test
    void emptyPostBodyReturnsArrayOfMessages() {
        ResponseEntity<String> response = apiRequestPost("/api/auth/login", null, null, String.class);

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("\"messages\":[");
    }

    @Test
    void invalidApiUrlReturns404AndJson() {
        ResponseEntity<String> response = apiRequestGet("/api/invalid");

        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);

        assert response.getBody() != null;
        assert response.getBody().contains("\"messages\":[");
    }

}
