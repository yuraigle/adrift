package ru.orlov.adrift;


import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdListingTests extends AbstractTest {

    @Test
    void publicAdApiShownWithoutToken() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        ResponseEntity<String> response = apiRequestGet(
                "/api/ads/" + id, null, String.class
        );

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().contains("\"title\":");
    }

    @Test
    void publicAdApiShownWithInvalidJwt() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        ResponseEntity<String> response = apiRequestGet(
                "/api/ads/" + id, "QWERTY", String.class
        );

        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() != null;
        assert response.getBody().contains("\"title\":");
    }

    @Test
    void publicAdHtmlShown() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        ResponseEntity<String> response = apiRequestGet(
                "/a/" + id, null, String.class
        );

        int statusCode = response.getStatusCode().value();
        assert List.of(200, 404).contains(statusCode); // not pre-rendered
    }

}
