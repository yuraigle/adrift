package ru.orlov.adrift;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Log4j2
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdImageTests extends AbstractTest {

    private final ByteArrayResource testImageResource =
            new ByteArrayResource("test".getBytes()) {
                @Override
                public String getFilename() {
                    return "test_image.jpg"; // Important: provide a filename
                }
            };

    @Test
    public void uploadAdImageUnauthorized() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "multipart/form-data");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", testImageResource);

        ResponseEntity<String> response = restTemplate.postForEntity(
                url("/api/ads/" + id + "/images"),
                new HttpEntity<>(body, headers),
                String.class
        );

        assert response.getStatusCode() == HttpStatus.UNAUTHORIZED;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);
    }

    @Test
    public void uploadAdImageInvalidToken() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "multipart/form-data");
        headers.add("Authorization", "Bearer QWERTY");

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", testImageResource);

        ResponseEntity<String> response = restTemplate.postForEntity(
                url("/api/ads/" + id + "/images"),
                new HttpEntity<>(body, headers),
                String.class
        );

        assert response.getStatusCode() == HttpStatus.BAD_REQUEST;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);
    }

    @Test
    public void uploadAdImageOthersAd() throws Exception {
        Long id = generateRandomTestAd();
        assert id != null;

        String token = retrieveTesterToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "multipart/form-data");
        headers.add("Authorization", "Bearer " + token);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", testImageResource);

        ResponseEntity<String> response = restTemplate.postForEntity(
                url("/api/ads/" + id + "/images"),
                new HttpEntity<>(body, headers),
                String.class
        );

        assert response.getStatusCode() == HttpStatus.FORBIDDEN;
        assert response.getHeaders().getContentType() != null;
        assert response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON);
    }
}
