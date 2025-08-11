package ru.orlov.adrift;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.orlov.adrift.controller.dto.AdRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdCreatingTests extends AbstractTest {

    @Test
    void createAdUnauthorized() {
        AdRequestDto form = new AdRequestDto();
        form.setTitle("Test AD #0");
        form.setCategory(1L);
        ResponseEntity<String> response = apiRequestPost("/api/ads", form, null, String.class);

        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
    }

    @Test
    void createAdMalformed() {
        AdRequestDto form = new AdRequestDto();
        form.setTitle(null);
        form.setCategory(0L);

        String token = retrieveToken("admin", "admin");
        ResponseEntity<String> response = apiRequestPost("/api/ads", form, token, String.class);

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getBody() != null;
        assert response.getBody().contains("messages");
        assert response.getBody().contains("title");
        assert response.getBody().contains("category");
    }

    @Test
    void createAdSuccessContainsCreatedId() {
        AdRequestDto form = new AdRequestDto();
        form.setTitle("Test AD #0");
        form.setDescription("Test description");
        form.setCategory(1L);

        String token = retrieveToken("admin", "admin");
        ResponseEntity<String> response = apiRequestPost("/api/ads", form, token, String.class);

        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getBody() != null;
        assert response.getBody().contains("\"id\":");
    }

}
