package ru.orlov.adrift;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdCreatingTests extends AbstractTest {

    @Test
    void createAdUnauthorized() {
        Map<String, String> form = Map.of("title", "Test AD #0", "category", "1");
        ResponseEntity<String> response = testRequestPost("/api/ads", form);

        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    void createAdMalformed() {
        String token = retrieveToken("admin", "admin");
        Map<String, String> form = Map.of("title", "", "category", "0");
        ResponseEntity<String> response = testRequestPost("/api/ads", form, token);

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getBody() != null;
        assert response.getBody().contains("messages");
        assert response.getBody().contains("title");
        assert response.getBody().contains("category");
    }

    @Test
    void createAd() {
        String token = retrieveToken("admin", "admin");
        Map<String, String> form = Map.of("title", "Test AD #1", "category", "1");
        ResponseEntity<String> response = testRequestPost("/api/ads", form, token);

        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getBody() != null;
        assert response.getBody().contains("\"id\":");
    }

}
